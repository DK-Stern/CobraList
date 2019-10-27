package sh.stern.cobralist.position.music.request.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.position.music.request.api.MusicRequestPositionService;
import sh.stern.cobralist.position.music.request.dataaccess.port.MusicRequestPositionDTO;
import sh.stern.cobralist.position.music.request.dataaccess.port.MusicRequestPositionDataService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MusicRequestPositionPublicApiService implements MusicRequestPositionService {

    private final MusicRequestPositionDataService musicRequestPositionDataService;

    @Autowired
    public MusicRequestPositionPublicApiService(MusicRequestPositionDataService musicRequestPositionDataService) {
        this.musicRequestPositionDataService = musicRequestPositionDataService;
    }

    @Override
    public int calculateMusicRequestPosition(Long playlistId, Long musicRequestId, int rating, boolean isDownVote) {
        return isDownVote
                ? calculatePositionOfMusicRequestForDownVoting(playlistId, musicRequestId, rating)
                : calculatePositionOfMusicRequestForUpVoting(playlistId, musicRequestId, rating);
    }


    private int calculatePositionOfMusicRequestForUpVoting(Long playlistId, Long musicRequestId, int newRating) {
        if (musicRequestPositionDataService.isPlaylistEmpty(playlistId)) {
            return 0;
        }

        final List<MusicRequestPositionDTO> musicRequests = musicRequestPositionDataService.getMusicRequestWithSameRatingForUpVote(playlistId, newRating);
        if (musicRequests.isEmpty()) {
            final int topRating = musicRequestPositionDataService.getTopRatingInPlaylist(playlistId);
            if (topRating > newRating) {
                return getLowestPositionOfMusicRequestWithRating(playlistId, newRating + 1, false) + 1;
            } else {
                return 0;
            }
        }

        return getPositionOfLastMusicRequestWithUpVotesLowerOrEqual(musicRequestId, musicRequests, false);
    }

    private int calculatePositionOfMusicRequestForDownVoting(Long playlistId, Long musicRequestId, int newRating) {
        if (musicRequestPositionDataService.isPlaylistEmpty(playlistId)) {
            return 0;
        }

        final List<MusicRequestPositionDTO> musicRequests = musicRequestPositionDataService.getMusicRequestWithSameRatingForDownVote(playlistId, newRating);
        if (musicRequests.isEmpty()) {
            final int worstRatingInPlaylist = musicRequestPositionDataService.getWorstRatingInPlaylist(playlistId);
            if (worstRatingInPlaylist < newRating) {
                return getHighestPositionOfMusicRequestWithRating(playlistId, newRating - 1, true) - 1;
            } else {
                return musicRequestPositionDataService.getPositionOfLastMusicRequest(playlistId);
            }
        }

        return getPositionOfLastMusicRequestWithUpVotesLowerOrEqual(musicRequestId, musicRequests, true) - 1;
    }

    private int getLowestPositionOfMusicRequestWithRating(Long playlistId, int rating, boolean isDownVote) {
        final List<MusicRequestPositionDTO> musicRequests = musicRequestPositionDataService.getMusicRequestWithSameRatingForUpVote(playlistId, rating);
        return musicRequests.stream().findFirst()
                .map(MusicRequestPositionDTO::getPosition)
                .orElseGet(() -> getLowestPositionOfMusicRequestWithRating(playlistId, isDownVote ? rating - 1 : rating + 1, isDownVote));
    }

    private int getHighestPositionOfMusicRequestWithRating(Long playlistId, int rating, boolean isDownVote) {
        final List<MusicRequestPositionDTO> musicRequests = musicRequestPositionDataService.getMusicRequestWithSameRatingForUpVote(playlistId, rating);
        return musicRequests.stream()
                .min(Comparator.comparingInt(MusicRequestPositionDTO::getPosition))
                .map(MusicRequestPositionDTO::getPosition)
                .orElseGet(() -> getHighestPositionOfMusicRequestWithRating(playlistId, isDownVote ? rating - 1 : rating + 1, isDownVote));
    }

    private int getPositionOfLastMusicRequestWithUpVotesLowerOrEqual(Long musicRequestId, List<MusicRequestPositionDTO> musicRequestPositionDTOS, boolean isDownVote) {
        final int upVotes = isDownVote
                ? musicRequestPositionDataService.getUpVotes(musicRequestId)
                : musicRequestPositionDataService.getUpVotes(musicRequestId) + 1;

        return musicRequestPositionDTOS.stream()
                .filter(musicRequestPositionDTO -> musicRequestPositionDTO.getUpVotes() >= upVotes)
                .min(Comparator.comparingInt(MusicRequestPositionDTO::getUpVotes))
                .map(musicRequestPositionDTO -> musicRequestPositionDTO.getPosition() + 1)
                .orElseGet(() -> musicRequestPositionDTOS.stream()
                        .min(Comparator.comparingInt(MusicRequestPositionDTO::getPosition))
                        .map(MusicRequestPositionDTO::getPosition)
                        .orElse(0));
    }

    @Override
    public int getPositionOfLastMusicRequest(Long playlistId) {
        return musicRequestPositionDataService.getPositionOfLastMusicRequest(playlistId);
    }

    @Override
    public Optional<Integer> getPositionOfMusicRequestWithNegativeRatingAndLowestPosition(Long playlistId) {
        return musicRequestPositionDataService.getPositionOfMusicRequestWithNegativeRatingAndLowestPosition(playlistId);
    }

    @Override
    public Long getPlaylistId(String partyCode) {
        return musicRequestPositionDataService.getPlaylistId(partyCode);
    }

    @Override
    public void persistNewMusicRequest(Long playlistId, TrackDTO trackDTO, int position) {
        musicRequestPositionDataService.incrementMusicRequestPositions(playlistId, position);
        musicRequestPositionDataService.saveMusicRequest(playlistId, trackDTO, position);
    }

    @Override
    public int decreaseMusicRequestPositions(Long playlistId, int position) {
        return musicRequestPositionDataService.decrementMusicRequestPositions(playlistId, position);
    }

    @Override
    public void updateMusicRequestPosition(Long musicRequestId, Long playlistId, int oldPosition, int newPosition) {
        if (oldPosition > newPosition) {
            musicRequestPositionDataService.incrementMusicRequestPositionInterval(playlistId, newPosition, oldPosition);
            musicRequestPositionDataService.updateMusicRequestPosition(musicRequestId, newPosition);
        } else if (oldPosition < newPosition) {
            musicRequestPositionDataService.decrementMusicRequestPositionInterval(playlistId, oldPosition, newPosition);
            musicRequestPositionDataService.updateMusicRequestPosition(musicRequestId, newPosition);
        }
    }

    @Override
    public boolean isPlaylistEmpty(Long playlistId) {
        return musicRequestPositionDataService.isPlaylistEmpty(playlistId);
    }
}
