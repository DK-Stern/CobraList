package sh.stern.cobralist.party.music.requests;

import java.util.List;

public interface MusicRequestService {
    List<MusicRequestDTO> getMusicRequests(String partyCode, Long userId, boolean isUser);
}
