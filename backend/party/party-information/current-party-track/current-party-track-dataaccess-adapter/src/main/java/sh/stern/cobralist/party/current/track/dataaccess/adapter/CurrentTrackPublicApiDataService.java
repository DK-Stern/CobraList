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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrentTrackPublicApiDataService implements CurrentTrackDataService {

    private final PartyRepository partyRepository;
    private final MusicRequestRepository musicRequestRepository;

    @Autowired
    public CurrentTrackPublicApiDataService(PartyRepository partyRepository,
                                            MusicRequestRepository musicRequestRepository) {
        this.partyRepository = partyRepository;
        this.musicRequestRepository = musicRequestRepository;
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
    public boolean hasMusicRequestStatusPlayed(String partyCode, String trackId) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));

        final Optional<MusicRequest> musicRequestOptional = musicRequestRepository.findByPlaylistAndTrackId(party.getPlaylist(), trackId);

        return !musicRequestOptional.isPresent() || musicRequestOptional.get().getPlayed();
    }

    @Override
    public void changeMusicRequestPlayedStatus(String partyCode, String trackId, boolean isPlayedStatus) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        final MusicRequest musicRequest = musicRequestRepository.findByPlaylistAndTrackId(party.getPlaylist(), trackId)
                .orElseThrow(() -> new MusicRequestNotFoundException(party.getPlaylist().getId(), trackId));
        musicRequest.setPlayed(isPlayedStatus);
        if (isPlayedStatus) {
            resetRatingAndPosition(musicRequest);
        }
        musicRequestRepository.saveAndFlush(musicRequest);
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
}
