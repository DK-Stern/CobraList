package sh.stern.cobralist.party.current.track.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.current.track.api.CurrentTrackService;
import sh.stern.cobralist.party.current.track.dataaccess.port.CurrentTrackDataService;
import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;
import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;
import sh.stern.cobralist.streaming.api.CurrentTrackStreamingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        handleTrackIsPlayedStatus(activeParties);
    }

    private void handleTrackIsPlayedStatus(List<ActivePartyDTO> activeParties) {
        this.partyPlaybacks.forEach((partyCode, currentPlaybackDTO) -> {
            Long playlistId = currentTrackDataService.getPlaylistId(partyCode);
            final TrackDTO currentTrack = currentPlaybackDTO.getCurrentTrack();
            if (currentTrack != null) {
                Long musicRequestId = currentTrackDataService.getMusicRequestId(playlistId, currentTrack.getStreamingId());
                final boolean isPlayed = currentTrackDataService.hasMusicRequestStatusPlayed(musicRequestId);
                if (!isPlayed) {
                    final Optional<String> username = activeParties.stream()
                            .filter(activePartyDTO -> activePartyDTO.getPartyCode().equals(partyCode))
                            .map(ActivePartyDTO::getCreatorEmail)
                            .findFirst();
                    if (username.isPresent()) {
                        final String playlistStreamingId = currentTrackDataService.getPlaylistStreamingId(partyCode);
                        currentTrackStreamingService.removeTrackFromPlaylist(username.get(), playlistStreamingId, currentPlaybackDTO.getCurrentTrack().getStreamingId());
                        currentTrackDataService.changeMusicRequestPlayedStatus(musicRequestId, true);
                    }
                }
            }
        });
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
