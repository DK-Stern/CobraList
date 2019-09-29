package sh.stern.cobralist.add.music.request.api;

import sh.stern.cobralist.user.userprincipal.UserPrincipal;

public interface AddMusicRequestService {
    void addMusicRequest(UserPrincipal userPrincipal, AddMusicRequestDTO addMusicRequestDTO);
}
