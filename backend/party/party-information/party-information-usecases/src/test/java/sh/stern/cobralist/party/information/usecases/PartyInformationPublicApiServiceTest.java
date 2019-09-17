package sh.stern.cobralist.party.information.usecases;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sh.stern.cobralist.party.current.track.api.CurrentTrackService;
import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;
import sh.stern.cobralist.party.information.domain.PartyInformationDTO;
import sh.stern.cobralist.party.music.requests.MusicRequestDTO;
import sh.stern.cobralist.party.music.requests.MusicRequestService;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.user.domain.UserRole;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("squid:S1192") // duplicate literals in tests are ok
@RunWith(MockitoJUnitRunner.class)
public class PartyInformationPublicApiServiceTest {

    private PartyInformationPublicApiService testSubject;

    @Mock
    private PartySecurityService partySecurityServiceMock;

    @Mock
    private CurrentTrackService currentTrackServiceMock;

    @Mock
    private MusicRequestService musicRequestServiceMock;

    @Before
    public void setUp() {
        testSubject = new PartyInformationPublicApiService(partySecurityServiceMock, currentTrackServiceMock, musicRequestServiceMock);
    }

    @Test
    public void verifyGetCurrentTrackCallsPartySecurityServiceForPartyParticipantValidation() {
        // given
        final String partyCode = "partyCode";
        final UserPrincipal userPrincipal = new UserPrincipal();

        // when
        testSubject.getPartyInformation(userPrincipal, partyCode);

        // then
        verify(partySecurityServiceMock).checkGetPartyInformationPermission(userPrincipal, partyCode);
    }

    @Test
    public void getCurrentTrackFromParty() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final String partyCode = "partyCode";

        final CurrentPlaybackDTO expectedCurrentPlaybackDTO = new CurrentPlaybackDTO();
        when(currentTrackServiceMock.getCurrentTrack(partyCode)).thenReturn(expectedCurrentPlaybackDTO);

        // when
        final PartyInformationDTO partyInformation = testSubject.getPartyInformation(userPrincipal, partyCode);

        // then
        assertThat(partyInformation.getCurrentPlayback()).isEqualTo(expectedCurrentPlaybackDTO);
    }

    @Test
    public void getMusicRequests() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final long userId = 1L;
        userPrincipal.setId(userId);
        userPrincipal.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())));
        final String partyCode = "partyCode";

        final MusicRequestDTO expectedDTO = new MusicRequestDTO();
        when(musicRequestServiceMock.getMusicRequests(partyCode, userId, true)).thenReturn(Collections.singletonList(expectedDTO));

        // when
        final PartyInformationDTO partyInformation = testSubject.getPartyInformation(userPrincipal, partyCode);

        // then
        assertThat(partyInformation.getMusicRequests()).containsExactly(expectedDTO);
    }
}