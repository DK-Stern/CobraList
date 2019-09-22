package sh.stern.cobralist.search.musicrequest.api;

import sh.stern.cobralist.search.musicrequest.domain.SearchMusicRequestDTO;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.List;

public interface SearchMusicRequestService {
    List<SearchMusicRequestDTO> searchMusicRequest(UserPrincipal userPrincipal, String partyCode, String searchString);
}
