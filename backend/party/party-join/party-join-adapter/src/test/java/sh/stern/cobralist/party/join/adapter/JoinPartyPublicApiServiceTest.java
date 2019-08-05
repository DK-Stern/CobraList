package sh.stern.cobralist.party.join.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sh.stern.cobralist.party.join.api.*;
import sh.stern.cobralist.party.join.dataaccess.port.GuestCreatedDTO;
import sh.stern.cobralist.party.join.dataaccess.port.JoinPartyDataService;
import sh.stern.cobralist.tokenprovider.TokenProvider;
import sh.stern.cobralist.user.domain.UserRole;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;
import sh.stern.cobralist.user.userprincipal.UserPrincipalBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JoinPartyPublicApiServiceTest {

    private JoinPartyPublicApiService testSubject;

    @Mock
    private TokenProvider tokenProviderMock;

    @Mock
    private JoinPartyDataService joinPartyDataServiceMock;

    @Mock
    private UserPrincipalBuilder userPrincipalBuilderMock;

    @Before
    public void setUp() throws Exception {
        testSubject = new JoinPartyPublicApiService(joinPartyDataServiceMock, tokenProviderMock, userPrincipalBuilderMock);
    }

    @Test
    public void createGuestWithAccessToken() {
        // given
        final String partyCode = "A123D5";
        final long guestId = 1L;
        final String guestName = "Bob";
        final String partyPassword = "123";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setPartyPassword(partyPassword);
        joinPartyDto.setGuestName(guestName);

        when(joinPartyDataServiceMock.getPartyPassword(partyCode)).thenReturn(partyPassword);

        final GuestCreatedDTO guestCreatedDTO = new GuestCreatedDTO();
        guestCreatedDTO.setGuestId(guestId);
        guestCreatedDTO.setPartyCode(partyCode);
        guestCreatedDTO.setName(guestName);
        when(joinPartyDataServiceMock.createGuest(joinPartyDto)).thenReturn(guestCreatedDTO);

        final UserPrincipal userPrincipal = new UserPrincipal();

        when(userPrincipalBuilderMock.withUserId(guestId)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withName(guestName)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withAuthorities(anyList())).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.build()).thenReturn(userPrincipal);

        final String accessToken = "13245674896";
        when(tokenProviderMock.createToken(userPrincipal, UserRole.ROLE_GUEST)).thenReturn(accessToken);

        // when
        final PartyJoinedDTO resultedPartyJoinedDTO = testSubject.joinParty(joinPartyDto);

        // then
        assertThat(resultedPartyJoinedDTO.getToken()).isEqualTo(accessToken);
    }

    @Test
    public void createGuestWithPartyCode() {
        // given
        final String partyCode = "A123D5";
        final long guestId = 1L;
        final String guestName = "Bob";
        final String partyPassword = "123";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setPartyPassword(partyPassword);
        joinPartyDto.setGuestName(guestName);

        when(joinPartyDataServiceMock.getPartyPassword(partyCode)).thenReturn(partyPassword);

        final GuestCreatedDTO guestCreatedDTO = new GuestCreatedDTO();
        guestCreatedDTO.setPartyCode(partyCode);
        guestCreatedDTO.setGuestId(guestId);
        guestCreatedDTO.setName(guestName);
        when(joinPartyDataServiceMock.createGuest(joinPartyDto)).thenReturn(guestCreatedDTO);

        final UserPrincipal userPrincipal = new UserPrincipal();

        when(userPrincipalBuilderMock.withUserId(guestId)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withName(guestName)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withAuthorities(anyList())).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.build()).thenReturn(userPrincipal);

        final String accessToken = "13245674896";
        when(tokenProviderMock.createToken(userPrincipal, UserRole.ROLE_GUEST)).thenReturn(accessToken);

        // when
        final PartyJoinedDTO resultedPartyJoinedDTO = testSubject.joinParty(joinPartyDto);

        // then
        assertThat(resultedPartyJoinedDTO.getPartyCode()).isEqualTo(partyCode);
    }

    @Test
    public void createGuestWithAuthorityRoleGuest() {
        // given
        final String partyCode = "A123D5";
        final long guestId = 1L;
        final String partyPassword = "123";
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setPartyPassword(partyPassword);
        joinPartyDto.setGuestName(guestName);

        when(joinPartyDataServiceMock.getPartyPassword(partyCode)).thenReturn(partyPassword);

        final GuestCreatedDTO guestCreatedDTO = new GuestCreatedDTO();
        guestCreatedDTO.setPartyCode(partyCode);
        guestCreatedDTO.setGuestId(guestId);
        guestCreatedDTO.setName(guestName);
        when(joinPartyDataServiceMock.createGuest(joinPartyDto)).thenReturn(guestCreatedDTO);

        final UserPrincipal userPrincipal = new UserPrincipal();

        when(userPrincipalBuilderMock.withUserId(guestId)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withName(guestName)).thenReturn(userPrincipalBuilderMock);
        // noinspection unchecked
        final ArgumentCaptor<List<SimpleGrantedAuthority>> authorityArgumentCaptor = ArgumentCaptor.forClass(List.class);
        when(userPrincipalBuilderMock.withAuthorities(authorityArgumentCaptor.capture())).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.build()).thenReturn(userPrincipal);

        final String accessToken = "13245674896";
        when(tokenProviderMock.createToken(userPrincipal, UserRole.ROLE_GUEST)).thenReturn(accessToken);

        // when
        testSubject.joinParty(joinPartyDto);

        // then
        assertThat(authorityArgumentCaptor.getValue().get(0).getAuthority()).isEqualTo(UserRole.ROLE_GUEST.name());
    }

    @Test
    public void throwsExceptionIfGuestNameIsAlreadyTaken() {
        // given
        final String partyCode = "A123D5";
        final long guestId = 1L;
        final String guestName = "Bob";
        final String partyPassword = "123";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setGuestName(guestName);
        joinPartyDto.setPartyPassword(partyPassword);


        when(joinPartyDataServiceMock.getPartyPassword(partyCode)).thenReturn(partyPassword);

        when(joinPartyDataServiceMock.countGuestName(guestName)).thenReturn(1L);

        // when u. then
        assertThatCode(() -> testSubject.joinParty(joinPartyDto))
                .isExactlyInstanceOf(GuestAlreadyExistException.class)
                .hasMessage("Der Name ist bereits vergeben auf der Party.");

    }

    @Test
    public void throwsExceptionIfPartyPasswordIsWrong() {
        // when
        final String partyCode = "A123D5";
        final long guestId = 1L;
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setPartyPassword("12");
        joinPartyDto.setGuestName(guestName);

        when(joinPartyDataServiceMock.getPartyPassword(partyCode)).thenReturn("123");

        // then
        assertThatCode(() -> testSubject.joinParty(joinPartyDto))
                .isExactlyInstanceOf(PartyPasswordWrongException.class)
                .hasMessage("Passwort der Party ist falsch.");
    }

    @Test
    public void findParty() {
        // given
        final String partyCode = "A123D5";

        final FindPartyDTO expectedDto = new FindPartyDTO();
        when(joinPartyDataServiceMock.findParty(partyCode)).thenReturn(expectedDto);

        // when
        final FindPartyDTO resultedDto = testSubject.findParty(partyCode);

        // then
        assertThat(resultedDto).isEqualTo(expectedDto);
    }
}