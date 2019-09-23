package sh.stern.cobralist.search.music.request.api;

import sh.stern.cobralist.search.music.request.domain.SearchMusicRequestDTO;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.List;

public interface SearchMusicRequestService {
    List<SearchMusicRequestDTO> searchMusicRequest(UserPrincipal userPrincipal, String partyCode, String searchString);
}
