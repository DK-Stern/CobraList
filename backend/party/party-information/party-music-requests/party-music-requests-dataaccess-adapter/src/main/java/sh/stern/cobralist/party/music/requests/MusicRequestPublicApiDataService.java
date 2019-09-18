package sh.stern.cobralist.party.music.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.dataaccess.VoteRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.Vote;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicRequestPublicApiDataService implements MusicRequestDataService {

    private final PartyRepository partyRepository;
    private final MusicRequestRepository musicRequestRepository;
    private final VoteRepository voteRepository;
    private final MusicRequestToMusicRequestDTOMapper musicRequestToMusicRequestDTOMapper;

    @Autowired
    public MusicRequestPublicApiDataService(PartyRepository partyRepository,
                                            PlaylistRepository playlistRepository,
                                            MusicRequestRepository musicRequestRepository,
                                            VoteRepository voteRepository,
                                            MusicRequestToMusicRequestDTOMapper musicRequestToMusicRequestDTOMapper) {
        this.partyRepository = partyRepository;
        this.musicRequestRepository = musicRequestRepository;
        this.voteRepository = voteRepository;
        this.musicRequestToMusicRequestDTOMapper = musicRequestToMusicRequestDTOMapper;
    }

    @Override
    public Long getPlaylistId(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        return party.getPlaylist().getId();
    }

    @Override
    public List<MusicRequestDTO> getMusicRequests(Long playlistId, Long participantId, boolean isUser) {
        final List<MusicRequest> musicRequests = musicRequestRepository.findByPlaylist_Id(playlistId);

        List<Vote> votes;
        if (isUser) {
            votes = voteRepository.findByUser_Id(participantId);
        } else {
            votes = voteRepository.findByGuest_Id(participantId);
        }

        final List<Long> musicRequestVotes = votes.stream().map(vote -> vote.getMusicRequest().getId())
                .collect(Collectors.toList());

        return musicRequests.stream()
                .map(musicRequest -> {
                    final MusicRequestDTO musicRequestDTO = musicRequestToMusicRequestDTOMapper.map(musicRequest);
                    musicRequestDTO.setAlreadyVoted(musicRequestVotes.contains(musicRequest.getId()));
                    return musicRequestDTO;
                })
                .collect(Collectors.toList());
    }
}
