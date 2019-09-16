package sh.stern.cobralist.party.music.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.dataaccess.VoteRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

import java.util.List;
import java.util.Map;
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

        final Map<Long, Long> downVotes = musicRequests.stream()
                .collect(Collectors.toMap(MusicRequest::getId, musicRequest -> voteRepository.countByMusicRequest_IdAndIsDownVote(musicRequest.getId(), true)));

        return musicRequests.stream()
                .map(musicRequest -> {
                    final MusicRequestDTO musicRequestDTO = musicRequestToMusicRequestDTOMapper.map(musicRequest);
                    musicRequestDTO.setDownVotes(downVotes.get(musicRequest.getId()));
                    musicRequestDTO.setUpVotes(musicRequestDTO.getAllVotes() - musicRequestDTO.getDownVotes());
                    musicRequestDTO.setAlreadyVoted(isUser
                            ? voteRepository.countByMusicRequest_IdAndUser_Id(musicRequest.getId(), participantId) == 1
                            : voteRepository.countByMusicRequest_IdAndGuest_Id(musicRequest.getId(), participantId) == 1);
                    musicRequestDTO.setRating(musicRequestDTO.getUpVotes() - musicRequestDTO.getDownVotes());
                    return musicRequestDTO;
                })
                .collect(Collectors.toList());
    }
}
