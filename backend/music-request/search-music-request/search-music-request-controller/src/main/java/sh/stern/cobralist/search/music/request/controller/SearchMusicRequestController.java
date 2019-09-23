package sh.stern.cobralist.search.music.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.search.music.request.api.SearchMusicRequestService;
import sh.stern.cobralist.search.music.request.domain.SearchMusicRequestDTO;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/musicrequest")
public class SearchMusicRequestController {

    private final SearchMusicRequestService searchMusicRequestService;

    @Autowired
    public SearchMusicRequestController(SearchMusicRequestService searchMusicRequestService) {
        this.searchMusicRequestService = searchMusicRequestService;
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('USER','GUEST')")
    public ResponseEntity<List<SearchMusicRequestDTO>> searchMusicRequest(@CurrentUser UserPrincipal userPrincipal, @RequestBody SearchMusicRequestRequest request) {
        return ResponseEntity.ok(searchMusicRequestService.searchMusicRequest(userPrincipal, request.getPartyCode(), request.getSearchString()));
    }
}
