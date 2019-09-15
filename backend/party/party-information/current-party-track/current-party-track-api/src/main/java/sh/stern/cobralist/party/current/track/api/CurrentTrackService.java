package sh.stern.cobralist.party.current.track.api;

import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;

public interface CurrentTrackService {
    CurrentPlaybackDTO getCurrentTrack(String partyCode);
}
