package sh.stern.cobralist.party.current.track.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.current.track.api.CurrentTrackService;
import sh.stern.cobralist.party.current.track.dataaccess.port.CurrentTrackDataService;
import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;
import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;
import sh.stern.cobralist.streaming.api.CurrentTrackStreamingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrentTrackPublicApiService implements CurrentTrackService {

    private final CurrentTrackDataService currentTrackDataService;
    private final CurrentTrackStreamingService currentTrackStreamingService;
    private Map<String, CurrentPlaybackDTO> partyPlaybacks;

    @Autowired
    public CurrentTrackPublicApiService(CurrentTrackDataService currentTrackDataService,
                                        CurrentTrackStreamingService currentTrackStreamingService) {
        this.currentTrackDataService = currentTrackDataService;
        this.currentTrackStreamingService = currentTrackStreamingService;
        this.partyPlaybacks = new HashMap<>();
    }

    @Scheduled(fixedRate = 5000)
    public void refreshCurrentTracks() {
        final List<ActivePartyDTO> activeParties = currentTrackDataService.getActiveParties();
        this.partyPlaybacks = currentTrackStreamingService.getCurrentTrackFromParties(activeParties);
        checkPlaybackPlayingStatus();
    }

    private void checkPlaybackPlayingStatus() {
        partyPlaybacks.entrySet().stream().filter(entry -> !entry.getValue().getPlaying())
                .forEach(entry -> currentTrackDataService.changePartyActiveStatus(entry.getKey(), false));
    }

    @Override
    public CurrentPlaybackDTO getCurrentTrack(String partyCode) {
        return partyPlaybacks.getOrDefault(partyCode, null);
    }
}
