package sh.stern.cobralist.party.persistence.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.stern.cobralist.party.persistence.domain.Guest;
import sh.stern.cobralist.party.persistence.domain.Party;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Long countGuestByNameAndParty(String name, Party party);
}
