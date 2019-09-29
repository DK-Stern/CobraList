package sh.stern.cobralist.add.music.request.dataaccess.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.add.music.request.dataaccess.port.AddMusicRequestDataService;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Playlist;
import sh.stern.cobralist.party.persistence.exceptions.PlaylistNotFoundException;

import java.util.Optional;

@Service
public class AddMusicRequestPublicApiDataService implements AddMusicRequestDataService {
    private static final Logger LOG = LoggerFactory.getLogger(AddMusicRequestPublicApiDataService.class);


    private final PlaylistRepository playlistRepository;
    private final MusicRequestRepository musicRequestRepository;

    @Autowired
    public AddMusicRequestPublicApiDataService(PlaylistRepository playlistRepository,
                                               MusicRequestRepository musicRequestRepository) {
        this.playlistRepository = playlistRepository;
        this.musicRequestRepository = musicRequestRepository;
    }

    @Override
    public String getPlaylistStreamingId(Long playlistId) {
        final Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> {
                    LOG.error(String.format("Playlist mit der id '%d' konnte nicht gefunden werden! Somit kann kein neues Lied hinzugefuegt werden.", playlistId));
                    return new PlaylistNotFoundException(playlistId);
                });
        return playlist.getPlaylistId();
    }

    @Override
    public boolean doesMusicRequestExist(Long playlistId, String trackStreamingId) {
        final Optional<MusicRequest> musicRequestOptional = musicRequestRepository.findByPlaylist_IdAndTrackId(playlistId, trackStreamingId);
        return musicRequestOptional.isPresent() && !musicRequestOptional.get().getPlayed();
    }
}
