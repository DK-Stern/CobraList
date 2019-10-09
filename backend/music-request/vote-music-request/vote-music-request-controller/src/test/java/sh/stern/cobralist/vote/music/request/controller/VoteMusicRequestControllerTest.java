package sh.stern.cobralist.vote.music.request.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;
import sh.stern.cobralist.vote.music.request.api.VoteMusicRequestDTO;
import sh.stern.cobralist.vote.music.request.api.VoteMusicRequestService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class VoteMusicRequestControllerTest {

    private VoteMusicRequestController testSubject;

    @Mock
    private VoteMusicRequestService voteMusicRequestServiceMock;

    @Before
    public void setUp() {
        testSubject = new VoteMusicRequestController(voteMusicRequestServiceMock);
    }

    @Test
    public void voteMusicRequest() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final VoteMusicRequestDTO voteMusicRequestDTO = new VoteMusicRequestDTO();

        // when
        final ResponseEntity responseEntity = testSubject.voteMusicRequest(userPrincipal, voteMusicRequestDTO);

        // then
        verify(voteMusicRequestServiceMock).voteMusicRequest(userPrincipal, voteMusicRequestDTO);
        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }
}