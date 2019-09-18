package sh.stern.cobralist.party.persistence.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import sh.stern.cobralist.party.persistence.domain.Vote;

import java.util.List;

@SuppressWarnings("squid:S00100")
// query methods need underscores for attributes, so breaking java naming convention is ok
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByUser_Id(Long userID);

    List<Vote> findByGuest_Id(Long guestID);
}
