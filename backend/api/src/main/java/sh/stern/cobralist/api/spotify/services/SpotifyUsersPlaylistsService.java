package sh.stern.cobralist.api.spotify.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.api.domains.SimplePlaylistDomain;
import sh.stern.cobralist.api.exceptions.AccessTokenExpired;
import sh.stern.cobralist.api.interfaces.UsersPlaylistsService;
import sh.stern.cobralist.api.spotify.SpotifyApi;
import sh.stern.cobralist.api.spotify.valueobjects.PagingObject;
import sh.stern.cobralist.api.spotify.valueobjects.SimplifiedPlaylistObject;
import sh.stern.cobralist.user.domain.StreamingProvider;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotifyUsersPlaylistsService implements UsersPlaylistsService {

    private static final String API_URL = "https://api.spotify.com/v1/";
    private final SpotifyApi spotifyApi;

    @Autowired
    public SpotifyUsersPlaylistsService(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    @Override
    public List<SimplePlaylistDomain> getUsersPlaylists(String userName) {
        spotifyApi.setAuthentication(StreamingProvider.spotify, userName);

        PagingObject<SimplifiedPlaylistObject> pagingObject = getUserPlaylistFromSpotify(userName, String.format("%sme/playlists", API_URL));
        List<SimplifiedPlaylistObject> simplifiedPlaylistObjectList = pagingObject.getItems();

        // if more playlists exists, then fetch next playlists
        while (pagingObject.getNext() != null) {
            pagingObject = getUserPlaylistFromSpotify(userName, pagingObject.getNext());
            simplifiedPlaylistObjectList.addAll(pagingObject.getItems());
        }

        return simplifiedPlaylistObjectList.stream()
                .map(simplifiedPlaylistObject -> new SimplePlaylistDomain(simplifiedPlaylistObject.getId(),
                        simplifiedPlaylistObject.getName()))
                .collect(Collectors.toList());
    }

    private PagingObject<SimplifiedPlaylistObject> getUserPlaylistFromSpotify(String userName, String url) {
        try {
            return spotifyApi.getUserPlaylists(url);
        } catch (AccessTokenExpired expired) {
            return getUserPlaylistFromSpotify(userName, url);
        }
    }
}
