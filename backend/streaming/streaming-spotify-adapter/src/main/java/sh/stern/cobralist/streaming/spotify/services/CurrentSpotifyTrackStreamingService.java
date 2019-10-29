package sh.stern.cobralist.streaming.spotify.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;
import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;
import sh.stern.cobralist.streaming.api.CurrentTrackStreamingService;
import sh.stern.cobralist.streaming.exceptions.AccessTokenExpiredException;
import sh.stern.cobralist.streaming.spotify.SpotifyApi;
import sh.stern.cobralist.streaming.spotify.mapper.TrackValueObjectToTrackDTOMapper;
import sh.stern.cobralist.streaming.spotify.valueobjects.CurrentPlaybackObject;
import sh.stern.cobralist.user.domain.StreamingProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CurrentSpotifyTrackStreamingService implements CurrentTrackStreamingService {

    private static final Logger LOG = LoggerFactory.getLogger(CurrentSpotifyTrackStreamingService.class);
    private static final String API_PLAYBACK_URL = "https://api.spotify.com/v1/me/player";
    private final SpotifyApi spotifyApi;
    private final TrackValueObjectToTrackDTOMapper trackValueObjectToTrackDTOMapper;

    @Autowired
    public CurrentSpotifyTrackStreamingService(SpotifyApi spotifyApi,
                                               TrackValueObjectToTrackDTOMapper trackValueObjectToTrackDTOMapper) {
        this.spotifyApi = spotifyApi;
        this.trackValueObjectToTrackDTOMapper = trackValueObjectToTrackDTOMapper;
    }

    @Override
    public Map<String, CurrentPlaybackDTO> getCurrentTrackFromParties(List<ActivePartyDTO> activeParties) {
        final HashMap<String, CurrentPlaybackDTO> currentTrackFromParties = new HashMap<>();
        activeParties.forEach(party -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                LOG.warn("Get current track from party interrupted.", e);
                Thread.currentThread().interrupt();
            }

            final CurrentPlaybackObject currentPlayback;
            final TrackDTO trackDTO;

            final CurrentPlaybackDTO currentPlaybackDTO = new CurrentPlaybackDTO();
            try {
                currentPlayback = getCurrentPlayback(party.getCreatorEmail(), API_PLAYBACK_URL);

                trackDTO = trackValueObjectToTrackDTOMapper.map(currentPlayback.getItem());
                currentPlaybackDTO.setCurrentTrack(trackDTO);
                currentPlaybackDTO.setPlaying(currentPlayback.getPlaying());
                currentPlaybackDTO.setProgressMs(currentPlayback.getProgressMs());
            } catch (IllegalStateException e) {
                currentPlaybackDTO.setPlaying(false);
            }

            currentTrackFromParties.put(party.getPartyCode(), currentPlaybackDTO);
        });

        return currentTrackFromParties;
    }

    private CurrentPlaybackObject getCurrentPlayback(String username, String url) {
        try {
            spotifyApi.setAuthentication(StreamingProvider.spotify, username);
            return spotifyApi.getCurrentPlayback(url);
        } catch (AccessTokenExpiredException e) {
            return getCurrentPlayback(username, url);
        }
    }

    @Override
    public void removeTrackFromPlaylist(String username, String playlistStreamingId, String trackId) {
        final String url = String.format("https://api.spotify.com/v1/playlists/%s/tracks", playlistStreamingId);
        try {
            spotifyApi.setAuthentication(StreamingProvider.spotify, username);
            spotifyApi.removeTrackFromPlaylist(url, trackId);
        } catch (AccessTokenExpiredException e) {
            removeTrackFromPlaylist(username, playlistStreamingId, trackId);
        }

    }
}
