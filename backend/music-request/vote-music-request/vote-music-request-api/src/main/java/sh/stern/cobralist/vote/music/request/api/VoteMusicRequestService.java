package sh.stern.cobralist.vote.music.request.api;

import sh.stern.cobralist.user.userprincipal.UserPrincipal;

public interface VoteMusicRequestService {
    void voteMusicRequest(UserPrincipal userPrincipal, VoteMusicRequestDTO voteMusicRequestDTO);
}
