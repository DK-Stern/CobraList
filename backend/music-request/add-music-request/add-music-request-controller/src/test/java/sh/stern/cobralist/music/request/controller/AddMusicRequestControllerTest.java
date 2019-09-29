package sh.stern.cobralist.music.request.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestDTO;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AddMusicRequestControllerTest {

    private AddMusicRequestController testSubject;

    @Mock
    private AddMusicRequestService addMusicRequestServiceMock;

    @Before
    public void setUp() {
        testSubject = new AddMusicRequestController(addMusicRequestServiceMock);
    }

    @Test
    public void addMusicRequest() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();

        // when
        final ResponseEntity responseEntity = testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO);

        // then
        verify(addMusicRequestServiceMock).addMusicRequest(userPrincipal, addMusicRequestDTO);
        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }
}