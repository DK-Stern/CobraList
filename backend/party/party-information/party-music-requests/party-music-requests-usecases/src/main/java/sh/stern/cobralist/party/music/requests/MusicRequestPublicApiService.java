package sh.stern.cobralist.party.music.requests;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicRequestPublicApiService implements MusicRequestService {

    private final MusicRequestDataService musicRequestDataService;

    @Autowired
    public MusicRequestPublicApiService(MusicRequestDataService musicRequestDataService) {
        this.musicRequestDataService = musicRequestDataService;
    }

    @Override
    public List<MusicRequestDTO> getMusicRequests(String partyCode, Long userId, boolean isUser) {
        Long playlistId = musicRequestDataService.getPlaylistId(partyCode);
        return musicRequestDataService.getMusicRequests(playlistId, userId, isUser);
    }
}
