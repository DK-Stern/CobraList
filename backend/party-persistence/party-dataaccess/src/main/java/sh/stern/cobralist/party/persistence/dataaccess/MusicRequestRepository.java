package sh.stern.cobralist.party.persistence.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Playlist;

import java.util.List;
import java.util.Optional;

// query methods need underscores for attributes, so breaking java naming convention is ok
@SuppressWarnings("squid:S00100")
@Repository
public interface MusicRequestRepository extends JpaRepository<MusicRequest, Long> {
    List<MusicRequest> findByPlaylist_Id(Long playlistId);

    Optional<MusicRequest> findByPlaylistAndTrackId(Playlist playlist, String trackId);

    Optional<MusicRequest> findByPlaylist_IdAndTrackId(Long playlistId, String trackId);

    Optional<MusicRequest> findByPlaylist_IdAndPosition(Long playlistId, int position);

    Optional<MusicRequest> findTopByPlaylist_IdAndRatingOrderByPositionDesc(Long playlistId, int rating);

    @Modifying
    @Query(
            value = "UPDATE music_request AS mr SET mr.position = mr.position + 1 WHERE mr.playlist_id = ?1 AND mr.position >= ?2",
            nativeQuery = true)
    int increasePositions(Long playlistId, int position);
}
