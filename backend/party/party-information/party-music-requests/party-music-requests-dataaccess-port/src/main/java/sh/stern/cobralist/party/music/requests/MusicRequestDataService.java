package sh.stern.cobralist.party.music.requests;

import java.util.List;

public interface MusicRequestDataService {
    Long getPlaylistId(String partyCode);

    List<MusicRequestDTO> getMusicRequests(Long playlistId, Long participantId, boolean isUser);
}
