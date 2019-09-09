package sh.stern.cobralist.party.persistence.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.stern.cobralist.party.persistence.domain.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
