package sh.stern.cobralist.position.music.request.dataaccess.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.Playlist;
import sh.stern.cobralist.party.persistence.exceptions.MusicRequestNotFoundException;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.party.persistence.exceptions.PlaylistNotFoundException;
import sh.stern.cobralist.position.music.request.dataaccess.port.MusicRequestPositionDTO;
import sh.stern.cobralist.position.music.request.dataaccess.port.MusicRequestPositionDataService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MusicRequestPositionPublicApiDataService implements MusicRequestPositionDataService {

    private static final Logger LOG = LoggerFactory.getLogger(MusicRequestPositionPublicApiDataService.class);

    private final MusicRequestRepository musicRequestRepository;
    private final PlaylistRepository playlistRepository;
    private final PartyRepository partyRepository;

    @Autowired
    public MusicRequestPositionPublicApiDataService(MusicRequestRepository musicRequestRepository,
                                                    PlaylistRepository playlistRepository,
                                                    PartyRepository partyRepository) {
        this.musicRequestRepository = musicRequestRepository;
        this.playlistRepository = playlistRepository;
        this.partyRepository = partyRepository;
    }

    @Override
    public int getPositionOfLastMusicRequest(Long playlistId) {
        return musicRequestRepository.findTopByPlaylist_IdOrderByPositionDesc(playlistId).map(MusicRequest::getPosition).orElse(0);
    }

    @Override
    public List<MusicRequestPositionDTO> getMusicRequestWithSameRatingForUpVote(Long playlistId, int rating) {
        return musicRequestRepository.findByPlaylist_IdAndRatingAndIsPlayedOrderByUpVotesAsc(playlistId, rating, false)
                .stream()
                .map(currentMusicRequest -> new MusicRequestPositionDTO(currentMusicRequest.getId(), currentMusicRequest.getPosition(), currentMusicRequest.getRating(), currentMusicRequest.getUpVotes()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MusicRequestPositionDTO> getMusicRequestWithSameRatingForDownVote(Long playlistId, int rating) {
        return musicRequestRepository.findByPlaylist_IdAndRatingAndIsPlayedOrderByUpVotesAscPositionAsc(playlistId, rating, false)
                .stream()
                .map(foundedMusicRequest -> new MusicRequestPositionDTO(foundedMusicRequest.getId(), foundedMusicRequest.getPosition(), foundedMusicRequest.getRating(), foundedMusicRequest.getUpVotes()))
                .collect(Collectors.toList());
    }

    @Override
    public int getTopRatingInPlaylist(Long playlistId) {
        final Optional<MusicRequest> topMusicRequest = musicRequestRepository.findByPlaylist_IdAndPosition(playlistId, 0);
        return topMusicRequest.map(MusicRequest::getRating).orElse(0);
    }

    @Override
    public int getWorstRatingInPlaylist(Long playlistId) {
        final Optional<MusicRequest> musicRequest = musicRequestRepository.findTopByPlaylist_IdOrderByRatingAsc(playlistId);
        return musicRequest.map(MusicRequest::getRating).orElse(0);
    }

    @Override
    public boolean isPlaylistEmpty(Long playlistId) {
        return !musicRequestRepository.findTopByPlaylist_IdAndIsPlayedOrderByPositionAsc(playlistId, false).isPresent();
    }

    @Override
    public int getUpVotes(Long musicRequestId) {
        return musicRequestRepository.findById(musicRequestId).map(MusicRequest::getUpVotes)
                .orElseThrow(() -> new MusicRequestNotFoundException(musicRequestId));
    }

    @Override
    public Optional<Integer> getPositionOfMusicRequestWithNegativeRatingAndLowestPosition(Long playlistId) {
        return musicRequestRepository.findFirstByPlaylist_IdAndAndIsPlayedAndRatingLessThanOrderByPositionAsc(playlistId, false, 0)
                .map(MusicRequest::getPosition);
    }

    @Override
    public Long getPlaylistId(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        return party.getPlaylist().getId();
    }

    @Override
    @Transactional
    public int incrementMusicRequestPositions(Long playlistId, int position) {
        return musicRequestRepository.incrementPositions(playlistId, position);
    }

    @Override
    @Transactional
    public int incrementMusicRequestPositionInterval(Long playlistId, int startPosition, int endPosition) {
        return musicRequestRepository.incrementPositionInterval(playlistId, startPosition, endPosition);
    }

    @Override
    @Transactional
    public int decrementMusicRequestPositions(Long playlistId, int position) {
        return musicRequestRepository.decrementMusicRequestPositions(playlistId, position);
    }

    @Override
    @Transactional
    public int decrementMusicRequestPositionInterval(Long playlistId, int startPosition, int endPosition) {
        return musicRequestRepository.decrementMusicRequestPositionInterval(playlistId, startPosition, endPosition);
    }

    @Override
    @Transactional
    public void saveMusicRequest(Long playlistId, TrackDTO trackDTO, int position) {
        final Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> {
                    LOG.error(String.format("Playlist mit der 'id' '%d' konnte nicht gefunden werden! Somit kann kein neues Lied hinzugefuegt werden.", playlistId));
                    return new PlaylistNotFoundException(playlistId);
                });

        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setTrackId(trackDTO.getStreamingId());
        musicRequest.setUri(trackDTO.getUri());
        musicRequest.setImageUrl(trackDTO.getImageUrl());
        musicRequest.setImageHeight(trackDTO.getImageHeight());
        musicRequest.setImageWidth(trackDTO.getImageWidth());
        musicRequest.setDuration(trackDTO.getDuration());
        musicRequest.setUpVotes(0);
        musicRequest.setDownVotes(0);
        musicRequest.setRating(0);
        musicRequest.setPosition(position);
        musicRequest.setPlayed(false);
        musicRequest.setPlaylist(playlist);
        musicRequest.setArtist(trackDTO.getArtists());
        musicRequest.setTitle(trackDTO.getName());
        musicRequestRepository.saveAndFlush(musicRequest);
    }

    @Override
    @Transactional
    public void updateMusicRequestPosition(Long musicRequestId, int newPosition) {
        final MusicRequest musicRequest = musicRequestRepository.findById(musicRequestId)
                .orElseThrow(() -> new MusicRequestNotFoundException(musicRequestId));
        musicRequest.setPosition(newPosition);
        musicRequestRepository.saveAndFlush(musicRequest);
    }
}
