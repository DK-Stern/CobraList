package sh.stern.cobralist.persistence.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.stern.cobralist.persistence.domain.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Long countGuestByName(String name);
}
