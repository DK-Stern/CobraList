package sh.stern.cobralist.api.spotify;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import sh.stern.cobralist.api.ApiBinding;
import sh.stern.cobralist.api.spotify.valueobjects.PagingObject;
import sh.stern.cobralist.api.spotify.valueobjects.SimplifiedPlaylistObject;

@Component
public class SpotifyApi extends ApiBinding {

    private static final String API_URL = "https://api.spotify.com/v1/";

    public PagingObject<SimplifiedPlaylistObject> getUserPlaylists() {
        return getUserPlaylists(String.format("%sme/playlists", API_URL));
    }

    public PagingObject<SimplifiedPlaylistObject> getUserPlaylists(String url) {
        return restTemplate
                .exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PagingObject<SimplifiedPlaylistObject>>() {
                        }
                ).getBody();
    }
}
