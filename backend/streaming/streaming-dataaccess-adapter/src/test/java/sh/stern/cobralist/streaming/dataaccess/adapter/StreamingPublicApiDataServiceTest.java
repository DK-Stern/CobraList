package sh.stern.cobralist.streaming.dataaccess.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.party.persistence.exceptions.UserNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StreamingPublicApiDataServiceTest {

    private StreamingPublicApiDataService testSubject;

    @Mock
    private UserRepository userRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new StreamingPublicApiDataService(userRepositoryMock);
    }

    @Test
    public void getUsersProviderId() {
        // given
        final long userId = 1234L;

        final User user = new User();
        final String expectedProviderId = "1231425dfs";
        user.setProviderId(expectedProviderId);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));

        // when
        final String resultedUsersProviderId = testSubject.getUsersProviderId(userId);

        // then
        assertThat(resultedUsersProviderId).isEqualTo(expectedProviderId);
    }

    @Test
    public void throwsExceptionOnGetUsersProviderIdIfUserNotExist() {
        // given
        final long userId = 1234L;

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // when u. then
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> testSubject.getUsersProviderId(userId))
                .withMessage("Benutzer mit der Id '" + userId + "' konnte nicht gefunden werden.");
    }
}