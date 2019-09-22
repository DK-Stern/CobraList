package sh.stern.cobralist.streaming.api;

import sh.stern.cobralist.party.creation.domain.TrackDTO;

import java.util.List;

public interface SearchTrackStreamingService {
    List<TrackDTO> searchTrack(String username, String searchString);
}
