package sh.stern.cobralist.party.persistence.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;

import java.util.List;
import java.util.Optional;

// query methods need underscores for attributes, so breaking java naming convention is ok
@SuppressWarnings("squid:S00100")
@Repository
public interface MusicRequestRepository extends JpaRepository<MusicRequest, Long> {
    List<MusicRequest> findByPlaylist_IdAndIsPlayed(Long playlistId, Boolean played);

    Optional<MusicRequest> findFirstByPlaylist_IdAndAndIsPlayedAndRatingLessThanOrderByPositionAsc(Long playlistId, Boolean isPlayed, Integer rating);

    Optional<MusicRequest> findByPlaylist_IdAndTrackIdAndIsPlayed(Long playlistId, String trackId, Boolean isPlayed);

    Optional<MusicRequest> findFirstByPlaylist_IdAndTrackIdAndIsPlayed(Long playlistId, String trackId, Boolean isPlayed);

    Optional<MusicRequest> findByPlaylist_IdAndPosition(Long playlistId, Integer position);

    List<MusicRequest> findByPlaylist_IdAndRatingOrderByUpVotesAsc(Long playlistId, Integer rating);

    List<MusicRequest> findByPlaylist_IdAndRatingOrderByUpVotesAscPositionAsc(Long playlistId, Integer rating);

    Optional<MusicRequest> findTopByPlaylist_IdOrderByRatingAsc(Long playlistId);

    Optional<MusicRequest> findTopByPlaylist_IdAndIsPlayedOrderByPositionAsc(Long playlistId, Boolean played);

    Optional<MusicRequest> findTopByPlaylist_IdOrderByPositionDesc(Long playlistId);

    @Modifying
    @Query(
            value = "UPDATE music_request AS mr SET mr.position = mr.position + 1 WHERE mr.playlist_id = ?1 AND mr.position >= ?2",
            nativeQuery = true)
    int incrementPositions(Long playlistId, int position);

    @Modifying
    @Query(
            value = "UPDATE music_request AS mr SET mr.position = mr.position + 1 WHERE mr.playlist_id = ?1 AND mr.position >= ?2 AND mr.position <= ?3",
            nativeQuery = true)
    int incrementPositionInterval(Long playlistId, int startPosition, int endPosition);

    @Modifying
    @Query(
            value = "UPDATE music_request AS mr SET mr.position = mr.position - 1 WHERE mr.playlist_id = ?1 AND mr.position >= ?2",
            nativeQuery = true)
    int decrementMusicRequestPositions(Long playlistId, int position);

    @Modifying
    @Query(
            value = "UPDATE music_request AS mr SET mr.position = mr.position - 1 WHERE mr.playlist_id = ?1 AND mr.position >= ?2 AND mr.position <= ?3",
            nativeQuery = true)
    int decrementMusicRequestPositionInterval(Long playlistId, Integer startPosition, Integer endPosition);

    int countById(Long musicRequestId);
}
