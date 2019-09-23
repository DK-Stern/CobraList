package sh.stern.cobralist.search.music.request.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.search.music.request.api.SearchMusicRequestService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@RunWith(MockitoJUnitRunner.class)
public class SearchMusicRequestControllerTest {

    private SearchMusicRequestController testSubject;

    @Mock
    private SearchMusicRequestService searchMusicRequestServiceMock;

    @Before
    public void setUp() {
        testSubject = new SearchMusicRequestController(searchMusicRequestServiceMock);
    }

    @Test
    public void searchMusicRequest() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();

        final SearchMusicRequestRequest request = new SearchMusicRequestRequest();
        final String partyCode = "asdqwe";
        request.setPartyCode(partyCode);
        final String searchString = "searchString";
        request.setSearchString(searchString);

        // when
        testSubject.searchMusicRequest(userPrincipal, request);

        // then
        Mockito.verify(searchMusicRequestServiceMock).searchMusicRequest(userPrincipal, partyCode, searchString);
    }
}