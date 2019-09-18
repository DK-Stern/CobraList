package sh.stern.cobralist.party.creation.dataaccess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.party.creation.dataaccess.adapter.mapper.PartyToPartyDTOMapper;
import sh.stern.cobralist.party.creation.dataaccess.port.PartyCreationDataService;
import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.party.creation.domain.PlaylistDTO;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.Playlist;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.party.persistence.exceptions.UserNotFoundException;

import java.util.Date;
import java.util.List;

@Service
public class PartyCreationPublicApiDataService implements PartyCreationDataService {

    private final PartyRepository partyRepository;
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final MusicRequestRepository musicRequestRepository;
    private final PartyToPartyDTOMapper partyToPartyDTOMapper;

    @Autowired
    public PartyCreationPublicApiDataService(PartyRepository partyRepository,
                                             PlaylistRepository playlistRepository,
                                             UserRepository userRepository,
                                             MusicRequestRepository musicRequestRepository,
                                             PartyToPartyDTOMapper partyToPartyDTOMapper) {
        this.partyRepository = partyRepository;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.musicRequestRepository = musicRequestRepository;
        this.partyToPartyDTOMapper = partyToPartyDTOMapper;
    }

    @Override
    public PartyDTO getParty(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));

        return partyToPartyDTOMapper.map(party);
    }

    @Override
    public PartyDTO createParty(Long userId, String partyName, String password, boolean downVoting, String description, String partyCode) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Party party = new Party();
        party.setPartyCode(partyCode);
        party.setUser(user);
        party.setCreationDate(new Date());
        party.setName(partyName);
        party.setPassword(password);
        party.setDownVotable(downVoting);
        party.setDescription(description);
        party.setActive(false);
        party.setArchived(false);
        party = partyRepository.save(party);

        return partyToPartyDTOMapper.map(party);
    }

    @Override
    @Transactional
    public void savePlaylistWithTracks(String partyCode, PlaylistDTO playlistDTO) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));

        Playlist playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setPlaylistId(playlistDTO.getPlaylistId());
        playlist.setParty(party);
        final Playlist savedPlaylist = playlistRepository.saveAndFlush(playlist);

        party.setPlaylist(playlist);
        partyRepository.saveAndFlush(party);

        final List<TrackDTO> tracks = playlistDTO.getTracks();
        for (int i = 0; i < tracks.size(); i++) {
            final TrackDTO track = tracks.get(i);
            final MusicRequest musicRequest = new MusicRequest();
            musicRequest.setTrackId(track.getId());
            musicRequest.setPosition(i);
            musicRequest.setUpVotes(0);
            musicRequest.setDownVotes(0);
            musicRequest.setPlayed(false);
            musicRequest.setImageUrl(track.getImageUrl());
            musicRequest.setImageWidth(track.getImageWidth());
            musicRequest.setImageHeight(track.getImageHeight());
            musicRequest.setTitle(track.getName());
            musicRequest.setArtist(track.getArtists());
            musicRequest.setUri(track.getUri());
            musicRequest.setPlaylist(savedPlaylist);
            musicRequest.setDuration(track.getDuration());
            musicRequestRepository.saveAndFlush(musicRequest);
        }
    }
}
