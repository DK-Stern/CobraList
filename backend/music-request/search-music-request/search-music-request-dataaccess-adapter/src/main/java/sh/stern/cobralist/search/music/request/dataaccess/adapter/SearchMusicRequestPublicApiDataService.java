package sh.stern.cobralist.search.music.request.dataaccess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.search.music.request.dataaccess.port.SearchMusicRequestDataService;

import java.util.Optional;

@Service
public class SearchMusicRequestPublicApiDataService implements SearchMusicRequestDataService {

    private final PartyRepository partyRepository;
    private final MusicRequestRepository musicRequestRepository;

    @Autowired
    public SearchMusicRequestPublicApiDataService(PartyRepository partyRepository,
                                                  MusicRequestRepository musicRequestRepository) {
        this.partyRepository = partyRepository;
        this.musicRequestRepository = musicRequestRepository;
    }

    @Override
    public Long getPlaylistId(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));

        return party.getPlaylist().getId();
    }

    @Override
    public boolean isMusicRequestAlreadyInPlaylist(Long playlistId, String trackId) {
        final Optional<MusicRequest> optionalMusicRequest = musicRequestRepository.findByPlaylist_IdAndTrackId(playlistId, trackId);

        return optionalMusicRequest.isPresent() && !optionalMusicRequest.get().getPlayed();
    }

    @Override
    public String getPartyCreatorStreamingId(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        return party.getUser().getEmail();
    }
}
