package sh.stern.cobralist.vote.music.request.dataaccess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.party.persistence.dataaccess.*;
import sh.stern.cobralist.party.persistence.domain.*;
import sh.stern.cobralist.party.persistence.exceptions.*;
import sh.stern.cobralist.vote.music.request.dataaccess.port.VoteMusicRequestDataService;

@Service
public class VoteMusicRequestPublicApiDataService implements VoteMusicRequestDataService {

    private final PartyRepository partyRepository;
    private final MusicRequestRepository musicRequestRepository;
    private final VoteRepository voteRepository;
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public VoteMusicRequestPublicApiDataService(PartyRepository partyRepository,
                                                MusicRequestRepository musicRequestRepository,
                                                VoteRepository voteRepository,
                                                GuestRepository guestRepository,
                                                UserRepository userRepository,
                                                PlaylistRepository playlistRepository) {
        this.partyRepository = partyRepository;
        this.musicRequestRepository = musicRequestRepository;
        this.voteRepository = voteRepository;
        this.guestRepository = guestRepository;
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
    }

    @Override
    public Long getPlaylistId(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        return party.getPlaylist().getId();
    }

    @Override
    public int countMusicRequest(Long musicRequestId) {
        return musicRequestRepository.countById(musicRequestId);
    }

    @Override
    public int countVotesFromParticipantForMusicRequest(Long participantId, boolean isGuest, Long musicRequestId) {
        if (isGuest) {
            return voteRepository.countByMusicRequest_IdAndGuest_Id(musicRequestId, participantId);
        } else {
            return voteRepository.countByMusicRequest_IdAndUser_Id(musicRequestId, participantId);
        }
    }

    @Override
    public String getPartyCreatorPrincipalUsername(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        return party.getUser().getEmail();
    }

    @Override
    public String getPlaylistStreamingId(Long playlistId) {
        final Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException(playlistId));
        return playlist.getPlaylistId();
    }

    @Override
    @Transactional
    public Integer updateMusicRequestsVote(Long musicRequestId, boolean downVote, Long participantId, boolean isGuest) {
        final MusicRequest musicRequest = musicRequestRepository.findById(musicRequestId)
                .orElseThrow(() -> new MusicRequestNotFoundException(musicRequestId));

        final Integer rating = musicRequest.getRating();
        if (downVote) {
            final Integer downVotes = musicRequest.getDownVotes();
            musicRequest.setRating(rating - 1);
            musicRequest.setDownVotes(downVotes + 1);
        } else {
            final Integer upVotes = musicRequest.getUpVotes();
            musicRequest.setRating(rating + 1);
            musicRequest.setUpVotes(upVotes + 1);
        }

        final Vote vote = new Vote();
        vote.setMusicRequest(musicRequest);
        vote.setDownVote(downVote);

        if (isGuest) {
            final Guest guest = guestRepository.findById(participantId)
                    .orElseThrow(() -> new GuestNotFoundException(participantId));
            vote.setGuest(guest);
        } else {
            final User user = userRepository.findById(participantId)
                    .orElseThrow(() -> new UserNotFoundException(participantId));
            vote.setUser(user);
        }
        voteRepository.saveAndFlush(vote);

        final MusicRequest savedMusicRequest = musicRequestRepository.saveAndFlush(musicRequest);
        return savedMusicRequest.getRating();
    }

    @Override
    public int getMusicRequestPosition(Long musicRequestId) {
        final MusicRequest musicRequest = musicRequestRepository.findById(musicRequestId)
                .orElseThrow(() -> new MusicRequestNotFoundException(musicRequestId));
        return musicRequest.getPosition();
    }

    @Override
    public int getMusicRequestRating(Long musicRequestId) {
        final MusicRequest musicRequest = musicRequestRepository.findById(musicRequestId)
                .orElseThrow(() -> new MusicRequestNotFoundException(musicRequestId));
        return musicRequest.getRating();
    }

    @Override
    public boolean isDownVotingAllowed(String partyCode) {
        return partyRepository.findByPartyCode(partyCode)
                .map(Party::isDownVotable)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
    }
}
