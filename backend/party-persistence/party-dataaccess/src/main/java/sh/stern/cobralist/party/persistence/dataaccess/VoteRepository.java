package sh.stern.cobralist.party.persistence.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import sh.stern.cobralist.party.persistence.domain.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Long countByMusicRequest_IdAndIsDownVote(Long musicRequestId, Boolean isDownVote);

    Long countByMusicRequest_IdAndUser_Id(Long musicRequestId, Long userId);

    Long countByMusicRequest_IdAndGuest_Id(Long musicRequestId, Long guestId);
}
