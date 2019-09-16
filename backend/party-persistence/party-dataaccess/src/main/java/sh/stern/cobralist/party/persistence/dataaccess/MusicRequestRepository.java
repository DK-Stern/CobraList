package sh.stern.cobralist.party.persistence.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;

import java.util.List;

@Repository
public interface MusicRequestRepository extends JpaRepository<MusicRequest, Long> {
    List<MusicRequest> findByPlaylist_Id(Long playlistId);
}
