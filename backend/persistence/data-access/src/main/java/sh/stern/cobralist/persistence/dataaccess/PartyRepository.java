package sh.stern.cobralist.persistence.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.stern.cobralist.persistence.domain.Party;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
}
