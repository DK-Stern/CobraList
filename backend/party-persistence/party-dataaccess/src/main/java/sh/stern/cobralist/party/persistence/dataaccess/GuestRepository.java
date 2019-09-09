package sh.stern.cobralist.party.persistence.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.stern.cobralist.party.persistence.domain.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Long countGuestByName(String name);
}
