package sh.stern.cobralist.streaming.spotify.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.streaming.api.PlayerStreamingService;
import sh.stern.cobralist.streaming.exceptions.AccessTokenExpiredException;
import sh.stern.cobralist.streaming.spotify.SpotifyApi;
import sh.stern.cobralist.user.domain.StreamingProvider;

@Service
public class SpotifyPlayerService implements PlayerStreamingService {

    public static final String API_URL = "https://api.spotify.com/v1/me/player";
    private final SpotifyApi spotifyApi;

    @Autowired
    public SpotifyPlayerService(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    @Override
    public void startPlaylist(String username, String playlistStreamingId) {
        try {
            spotifyApi.setAuthentication(StreamingProvider.spotify, username);
            spotifyApi.startPlaylist(String.format("%s/play", API_URL), playlistStreamingId);
        } catch (AccessTokenExpiredException e) {
            startPlaylist(username, playlistStreamingId);
        }
    }

    @Override
    public void stopPlaylist(String username) {
        try {
            spotifyApi.setAuthentication(StreamingProvider.spotify, username);
            spotifyApi.pausePlayback(String.format("%s/pause", API_URL));
        } catch (AccessTokenExpiredException e) {
            stopPlaylist(username);
        }
    }

    @Override
    public void skipSong(String username) {
        try {
            spotifyApi.setAuthentication(StreamingProvider.spotify, username);
            spotifyApi.skipSong(String.format("%s/next", API_URL));
        } catch (AccessTokenExpiredException e) {
            skipSong(username);
        }
    }
}
