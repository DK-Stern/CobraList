package sh.stern.cobralist.vote.music.request.usecases;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.position.music.request.api.MusicRequestPositionService;
import sh.stern.cobralist.streaming.api.PlaylistService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;
import sh.stern.cobralist.vote.music.request.api.VoteMusicRequestDTO;
import sh.stern.cobralist.vote.music.request.api.VoteMusicRequestDTONotFulfilledException;
import sh.stern.cobralist.vote.music.request.dataaccess.port.VoteMusicRequestDataService;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class VoteMusicRequestPublicApiServiceTest {

    private VoteMusicRequestPublicApiService testSubject;

    @Mock
    private PartySecurityService partySecurityServiceMock;

    @Mock
    private VoteMusicRequestDataService voteMusicRequestDataServiceMock;

    @Mock
    private MusicRequestPositionService musicRequestPositionServiceMock;

    @Mock
    private PlaylistService playlistServiceMock;

    @Before
    public void setUp() {
        testSubject = new VoteMusicRequestPublicApiService(partySecurityServiceMock,
                voteMusicRequestDataServiceMock,
                musicRequestPositionServiceMock,
                playlistServiceMock);
    }

    @Test
    public void throwsExceptionIfAttributeIsDownVoteOfVoteMusicRequestDTOIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final VoteMusicRequestDTO voteMusicRequestDTO = new VoteMusicRequestDTO();

        // when u. then
        Assertions.assertThatExceptionOfType(VoteMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.voteMusicRequest(userPrincipal, voteMusicRequestDTO))
                .withMessage("Request unvollstaendig. Attribut 'isDownVote' ist leer.");
    }

    @Test
    public void throwsExceptionIfAttributeTrackIdOfVoteMusicRequestDTOIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final VoteMusicRequestDTO voteMusicRequestDTO = new VoteMusicRequestDTO();
        voteMusicRequestDTO.setDownVote(false);

        // when u. then
        Assertions.assertThatExceptionOfType(VoteMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.voteMusicRequest(userPrincipal, voteMusicRequestDTO))
                .withMessage("Request unvollstaendig. Attribut 'trackId' ist leer.");
    }

    @Test
    public void throwsExceptionIfAttributePartyCodeOfVoteMusicRequestDTOIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final VoteMusicRequestDTO voteMusicRequestDTO = new VoteMusicRequestDTO();
        voteMusicRequestDTO.setDownVote(false);
        voteMusicRequestDTO.setMusicRequestId(12L);

        // when u. then
        Assertions.assertThatExceptionOfType(VoteMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.voteMusicRequest(userPrincipal, voteMusicRequestDTO))
                .withMessage("Request unvollstaendig. Attribut 'partyCode' ist leer.");
    }
}