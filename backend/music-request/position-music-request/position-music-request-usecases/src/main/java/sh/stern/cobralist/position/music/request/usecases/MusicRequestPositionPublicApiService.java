package sh.stern.cobralist.position.music.request.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.position.music.request.api.MusicRequestPositionService;
import sh.stern.cobralist.position.music.request.dataaccess.port.MusicRequestPositionDataService;

@Service
public class MusicRequestPositionPublicApiService implements MusicRequestPositionService {

    private final MusicRequestPositionDataService musicRequestPositionDataService;

    @Autowired
    public MusicRequestPositionPublicApiService(MusicRequestPositionDataService musicRequestPositionDataService) {
        this.musicRequestPositionDataService = musicRequestPositionDataService;
    }

    @Override
    public int calculateMusicRequestPosition(Long playlistId, int rating) {
        return musicRequestPositionDataService.getPositionOfLastMusicRequestWithRating(playlistId, rating) + 1;
    }

    @Override
    public Long getPlaylistId(String partyCode) {
        return musicRequestPositionDataService.getPlaylistId(partyCode);
    }

    @Override
    public void persistNewMusicRequest(Long playlistId, TrackDTO trackDTO, int position) {
        musicRequestPositionDataService.increaseMusicRequestPositions(playlistId, position);
        musicRequestPositionDataService.saveMusicRequest(playlistId, trackDTO, position);
    }
}
