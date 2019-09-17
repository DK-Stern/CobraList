package sh.stern.cobralist.party.join.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.join.api.GuestAlreadyExistException;
import sh.stern.cobralist.party.join.api.JoinPartyService;
import sh.stern.cobralist.party.join.api.PartyJoinedDTO;
import sh.stern.cobralist.party.join.api.PartyPasswordWrongException;
import sh.stern.cobralist.party.join.dataaccess.port.GuestCreatedDTO;
import sh.stern.cobralist.party.join.dataaccess.port.JoinPartyDataService;
import sh.stern.cobralist.party.join.domain.FindPartyDTO;
import sh.stern.cobralist.party.join.domain.JoinPartyDTO;
import sh.stern.cobralist.tokenprovider.TokenProvider;
import sh.stern.cobralist.user.domain.UserRole;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;
import sh.stern.cobralist.user.userprincipal.UserPrincipalBuilder;

import java.util.Collections;

@Service
public class JoinPartyPublicApiService implements JoinPartyService {

    private static final String GUEST_NAME_ERROR_MESSAGE = "Der Name ist bereits vergeben auf der Party.";
    @SuppressWarnings("squid:S2068") // is no credential
    private static final String PARTY_PASSWORD_ERROR_MESSAGE = "Passwort der Party ist falsch.";

    private final JoinPartyDataService joinPartyDataService;
    private final TokenProvider tokenProvider;
    private final UserPrincipalBuilder userPrincipalBuilder;

    @Autowired
    public JoinPartyPublicApiService(JoinPartyDataService joinPartyDataService,
                                     TokenProvider tokenProvider,
                                     UserPrincipalBuilder userPrincipalBuilder) {
        this.joinPartyDataService = joinPartyDataService;
        this.tokenProvider = tokenProvider;
        this.userPrincipalBuilder = userPrincipalBuilder;
    }

    @Override
    public PartyJoinedDTO joinParty(JoinPartyDTO joinPartyDto) {
        checkPartyPassword(joinPartyDto);
        checkGuestNameAlreadyExist(joinPartyDto);

        joinPartyDataService.getPartyPassword(joinPartyDto.getPartyCode());

        final GuestCreatedDTO newGuest = joinPartyDataService.createGuest(joinPartyDto);

        final UserPrincipal userPrincipal = userPrincipalBuilder.withUserId(newGuest.getGuestId())
                .withName(newGuest.getName())
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_GUEST.name())))
                .build();

        final String token = tokenProvider.createToken(userPrincipal, UserRole.ROLE_GUEST);
        final PartyJoinedDTO partyJoinedDTO = new PartyJoinedDTO();
        partyJoinedDTO.setToken(token);
        partyJoinedDTO.setPartyCode(joinPartyDto.getPartyCode());

        return partyJoinedDTO;
    }

    private void checkPartyPassword(JoinPartyDTO joinPartyDto) {
        final String partyPassword = joinPartyDataService.getPartyPassword(joinPartyDto.getPartyCode());
        if (!partyPassword.equals(joinPartyDto.getPartyPassword())) {
            throw new PartyPasswordWrongException(PARTY_PASSWORD_ERROR_MESSAGE);
        }
    }

    private void checkGuestNameAlreadyExist(JoinPartyDTO joinPartyDto) {
        final Long countedGuestName = joinPartyDataService.countGuestName(joinPartyDto.getGuestName(), joinPartyDto.getPartyCode());
        if (countedGuestName > 0L) {
            throw new GuestAlreadyExistException(GUEST_NAME_ERROR_MESSAGE);
        }
    }

    @Override
    public FindPartyDTO findParty(String partyCode) {
        return joinPartyDataService.findParty(partyCode);
    }
}
