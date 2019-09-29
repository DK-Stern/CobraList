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
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.party.persistence.exceptions.PlaylistNotFoundException;
import sh.stern.cobralist.position.music.request.dataaccess.port.MusicRequestPositionDataService;

import java.util.Optional;

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
    public int getPositionOfLastMusicRequestWithRating(Long playlistId, int rating) {
        final Optional<MusicRequest> lastMusicRequest = musicRequestRepository.findTopByPlaylist_IdAndRatingOrderByPositionDesc(playlistId, rating);

        if (!lastMusicRequest.isPresent()) {
            final long countEntries = musicRequestRepository.count();
            if (countEntries == 0) {
                return 0;
            }

            // music request with position 0 has the best rating
            final Optional<MusicRequest> topMusicRequest = musicRequestRepository.findByPlaylist_IdAndPosition(playlistId, 0);
            if (!topMusicRequest.isPresent()) {
                return 0;
            }
            final Integer topMusicRequestRating = topMusicRequest.get().getRating();
            if (topMusicRequestRating > rating) {
                return getPositionOfLastMusicRequestWithRating(playlistId, rating + 1);
            } else {
                return 0;
            }
        }

        return lastMusicRequest.get().getPosition();
    }

    @Override
    public Long getPlaylistId(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        return party.getPlaylist().getId();
    }

    @Override
    @Transactional
    public int increaseMusicRequestPositions(Long playlistId, int position) {
        return musicRequestRepository.increasePositions(playlistId, position);
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
        musicRequest.setTrackId(trackDTO.getId());
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
}
