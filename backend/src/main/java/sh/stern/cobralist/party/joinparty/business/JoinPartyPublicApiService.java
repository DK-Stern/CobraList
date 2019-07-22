package sh.stern.cobralist.party.joinparty.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.joinparty.api.JoinPartyDTO;
import sh.stern.cobralist.party.joinparty.api.JoinPartyService;
import sh.stern.cobralist.party.joinparty.api.PartyJoinedDTO;
import sh.stern.cobralist.party.joinparty.dataaccess.api.GuestCreatedDTO;
import sh.stern.cobralist.party.joinparty.dataaccess.api.JoinPartyDataService;
import sh.stern.cobralist.security.TokenProvider;
import sh.stern.cobralist.security.UserRole;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;
import sh.stern.cobralist.security.oauth2.user.UserPrincipalBuilder;

import java.util.Collections;

@Service
public class JoinPartyPublicApiService implements JoinPartyService {

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
        final GuestCreatedDTO newGuest = joinPartyDataService.createGuest(joinPartyDto);

        final UserPrincipal userPrincipal = userPrincipalBuilder.withId(newGuest.getId())
                .withName(newGuest.getName())
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_GUEST.name())))
                .build();

        final String token = tokenProvider.createToken(userPrincipal, UserRole.ROLE_GUEST);
        final PartyJoinedDTO partyJoinedDTO = new PartyJoinedDTO();
        partyJoinedDTO.setToken(token);
        partyJoinedDTO.setPartyId(joinPartyDto.getPartyId());

        return partyJoinedDTO;
    }
}
