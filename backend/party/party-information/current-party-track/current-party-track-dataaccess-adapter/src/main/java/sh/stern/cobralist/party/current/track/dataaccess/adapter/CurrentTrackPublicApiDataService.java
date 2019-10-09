package sh.stern.cobralist.party.current.track.dataaccess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.party.current.track.dataaccess.port.CurrentTrackDataService;
import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.exceptions.MusicRequestNotFoundException;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.position.music.request.api.MusicRequestPositionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrentTrackPublicApiDataService implements CurrentTrackDataService {

    private final PartyRepository partyRepository;
    private final MusicRequestRepository musicRequestRepository;
    private final MusicRequestPositionService musicRequestPositionService;

    @Autowired
    public CurrentTrackPublicApiDataService(PartyRepository partyRepository,
                                            MusicRequestRepository musicRequestRepository,
                                            MusicRequestPositionService musicRequestPositionService) {
        this.partyRepository = partyRepository;
        this.musicRequestRepository = musicRequestRepository;
        this.musicRequestPositionService = musicRequestPositionService;
    }

    @Override
    public List<ActivePartyDTO> getActiveParties() {
        final List<Party> activeParties = partyRepository.findByActive(true);

        return activeParties.stream()
                .map(party -> new ActivePartyDTO(party.getPartyCode(), party.getUser().getName(), party.getUser().getEmail(), party.getPlaylist().getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void changePartyActiveStatus(String partyCode, Boolean activeStatus) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        party.setActive(activeStatus);
        partyRepository.save(party);
    }

    @Override
    public boolean hasMusicRequestStatusPlayed(Long musicRequestId) {
        final Optional<MusicRequest> musicRequestOptional = musicRequestRepository.findById(musicRequestId);
        return !musicRequestOptional.isPresent() || musicRequestOptional.get().getPlayed();
    }

    @Override
    public void changeMusicRequestPlayedStatus(Long musicRequestId, boolean isPlayedStatus) {
        final MusicRequest musicRequest = musicRequestRepository.findById(musicRequestId)
                .orElseThrow(() -> new MusicRequestNotFoundException(musicRequestId));

        musicRequest.setPlayed(isPlayedStatus);
        if (isPlayedStatus) {
            resetRatingAndPosition(musicRequest);
            decrementMusicRequestPositions(musicRequest.getPlaylist().getId());
        }
        musicRequestRepository.saveAndFlush(musicRequest);
    }

    private void decrementMusicRequestPositions(Long playlistId) {
        musicRequestPositionService.decreaseMusicRequestPositions(playlistId, 0);
    }

    private void resetRatingAndPosition(MusicRequest musicRequest) {
        musicRequest.setPosition(null);
        musicRequest.setRating(0);
        musicRequest.setDownVotes(0);
        musicRequest.setUpVotes(0);
    }

    @Override
    public String getPlaylistStreamingId(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        return party.getPlaylist().getPlaylistId();
    }

    @Override
    public Long getMusicRequestId(Long playlistId, String streamingId) {
        return musicRequestRepository.findFirstByPlaylist_IdAndTrackId(playlistId, streamingId)
                .map(MusicRequest::getId)
                .orElseThrow(MusicRequestNotFoundException::new);
    }

    @Override
    public Long getPlaylistId(String partyCode) {
        return partyRepository.findByPartyCode(partyCode)
                .map(Party::getId)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
    }
}
