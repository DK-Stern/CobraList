package sh.stern.cobralist.party.security.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.security.api.NoPartyCreatorException;
import sh.stern.cobralist.party.security.api.NoPartyParticipantException;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.party.security.dataaccess.port.PartySecurityDataService;
import sh.stern.cobralist.user.domain.UserRole;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.Collection;

@Service
public class PartySecurityPublicApiService implements PartySecurityService {

    private final PartySecurityDataService partySecurityDataService;

    @Autowired
    public PartySecurityPublicApiService(PartySecurityDataService partySecurityDataService) {
        this.partySecurityDataService = partySecurityDataService;
    }

    @Override
    public void checkGetPartyInformationPermission(UserPrincipal userPrincipal, String partyCode) {
        final Collection<SimpleGrantedAuthority> authorities = userPrincipal.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority(UserRole.ROLE_GUEST.name()))) {
            final boolean guestPartyParticipant = partySecurityDataService.isGuestPartyParticipant(userPrincipal.getId(), partyCode);

            if (!guestPartyParticipant) {
                throw new NoPartyParticipantException("Gast ist der Party nicht zugehörig.");
            }

        } else if (authorities.contains(new SimpleGrantedAuthority(UserRole.ROLE_USER.name()))) {
            final boolean userPartyParticipant = partySecurityDataService.isUserPartyParticipant(userPrincipal.getId(), partyCode);

            if (!userPartyParticipant) {
                throw new NoPartyParticipantException("Benutzer ist der Party nicht zugehörig.");
            }
        }
    }

    @Override
    public void checkIsPartyCreator(UserPrincipal userPrincipal, String partyCode) {
        final boolean partyCreator = partySecurityDataService.isPartyCreator(userPrincipal.getId(), partyCode);
        if (!partyCreator) {
            throw new NoPartyCreatorException(userPrincipal.getId(), partyCode);
        }
    }
}
