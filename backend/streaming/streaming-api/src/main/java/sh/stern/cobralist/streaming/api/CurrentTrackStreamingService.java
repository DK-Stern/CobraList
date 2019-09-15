package sh.stern.cobralist.streaming.api;

import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;
import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;

import java.util.List;
import java.util.Map;

public interface CurrentTrackStreamingService {
    Map<String, CurrentPlaybackDTO> getCurrentTrackFromParties(List<ActivePartyDTO> activeParties);
}
