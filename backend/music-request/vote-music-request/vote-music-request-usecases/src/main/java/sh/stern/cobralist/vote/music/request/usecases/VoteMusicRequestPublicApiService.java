package sh.stern.cobralist.vote.music.request.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sh.stern.cobralist.party.persistence.exceptions.MusicRequestNotFoundException;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.position.music.request.api.MusicRequestPositionService;
import sh.stern.cobralist.streaming.api.PlaylistService;
import sh.stern.cobralist.user.domain.UserRole;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;
import sh.stern.cobralist.vote.music.request.api.*;
import sh.stern.cobralist.vote.music.request.dataaccess.port.VoteMusicRequestDataService;

import java.util.Objects;

@Service
public class VoteMusicRequestPublicApiService implements VoteMusicRequestService {

    private final PartySecurityService partySecurityService;
    private final VoteMusicRequestDataService voteMusicRequestDataService;
    private final MusicRequestPositionService musicRequestPositionService;
    private final PlaylistService playlistService;

    @Autowired
    public VoteMusicRequestPublicApiService(PartySecurityService partySecurityService,
                                            VoteMusicRequestDataService voteMusicRequestDataService,
                                            MusicRequestPositionService musicRequestPositionService,
                                            PlaylistService playlistService) {
        this.partySecurityService = partySecurityService;
        this.voteMusicRequestDataService = voteMusicRequestDataService;
        this.musicRequestPositionService = musicRequestPositionService;
        this.playlistService = playlistService;
    }

    @Override
    public void voteMusicRequest(UserPrincipal userPrincipal, VoteMusicRequestDTO voteMusicRequestDTO) {
        checkVoteMusicRequestDTO(voteMusicRequestDTO);
        final String partyCode = voteMusicRequestDTO.getPartyCode();
        partySecurityService.checkGetPartyInformationPermission(userPrincipal, partyCode);

        final Long musicRequestId = voteMusicRequestDTO.getMusicRequestId();
        final int countMusicRequest = voteMusicRequestDataService.countMusicRequest(musicRequestId);
        if (countMusicRequest == 0) {
            throw new MusicRequestNotFoundException(musicRequestId);
        }

        final boolean isDownVote = voteMusicRequestDTO.getDownVote();
        if (isDownVote) {
            final boolean downVotingAllowed = voteMusicRequestDataService.isDownVotingAllowed(partyCode);
            if (!downVotingAllowed) {
                throw new DownVotingNotAllowedException(partyCode);
            }
        }

        final boolean guest = isGuest(userPrincipal);
        final int countedVotes = voteMusicRequestDataService.countVotesFromParticipantForMusicRequest(userPrincipal.getId(), guest, musicRequestId);
        if (countedVotes > 0) {
            throw new AlreadyVotedException(musicRequestId);
        }

        final int musicRequestRating = voteMusicRequestDataService.getMusicRequestRating(musicRequestId);
        int newRating = isDownVote ? musicRequestRating - 1 : musicRequestRating + 1;

        final Long playlistId = voteMusicRequestDataService.getPlaylistId(partyCode);
        final int oldPosition = voteMusicRequestDataService.getMusicRequestPosition(musicRequestId);
        final int newPosition = musicRequestPositionService.calculateMusicRequestPosition(playlistId, musicRequestId, newRating, isDownVote);
        voteMusicRequestDataService.updateMusicRequestsVote(musicRequestId, isDownVote, userPrincipal.getId(), guest);
        if (oldPosition != newPosition) {
            musicRequestPositionService.updateMusicRequestPosition(musicRequestId, playlistId, oldPosition, newPosition);
            updateMusicRequestPositionInStreamingPlaylist(voteMusicRequestDTO, playlistId, oldPosition, isDownVote ? newPosition + 1 : newPosition);
        }
    }

    private void updateMusicRequestPositionInStreamingPlaylist(VoteMusicRequestDTO voteMusicRequestDTO, Long playlistId, int oldPosition, int newPosition) {
        final String partyCreatorStreamingId = voteMusicRequestDataService.getPartyCreatorPrincipalUsername(voteMusicRequestDTO.getPartyCode());
        final String playlistStreamingStreamingId = voteMusicRequestDataService.getPlaylistStreamingId(playlistId);
        playlistService.moveTrackPosition(partyCreatorStreamingId, playlistStreamingStreamingId, oldPosition, newPosition);
    }

    private boolean isGuest(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ROLE_GUEST.name()));
    }

    private void checkVoteMusicRequestDTO(VoteMusicRequestDTO voteMusicRequestDTO) {
        if (Objects.isNull(voteMusicRequestDTO.getDownVote())) {
            throw new VoteMusicRequestDTONotFulfilledException("isDownVote");
        } else if (Objects.isNull(voteMusicRequestDTO.getMusicRequestId())) {
            throw new VoteMusicRequestDTONotFulfilledException("trackId");
        } else if (!StringUtils.hasText(voteMusicRequestDTO.getPartyCode())) {
            throw new VoteMusicRequestDTONotFulfilledException("partyCode");
        }
    }
}
