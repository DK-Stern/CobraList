package sh.stern.cobralist.api.spotify.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.api.domains.SimplePlaylistDomain;
import sh.stern.cobralist.api.interfaces.UsersPlaylistsService;
import sh.stern.cobralist.api.spotify.SpotifyApi;
import sh.stern.cobralist.api.spotify.valueobjects.PagingObject;
import sh.stern.cobralist.api.spotify.valueobjects.SimplifiedPlaylistObject;
import sh.stern.cobralist.user.domain.AuthProvider;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotifyUsersPlaylistsService implements UsersPlaylistsService {

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final SpotifyApi spotifyApi;

    @Autowired
    public SpotifyUsersPlaylistsService(OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                                        SpotifyApi spotifyApi) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.spotifyApi = spotifyApi;
    }

    @Override
    public List<SimplePlaylistDomain> getUsersPlaylists(String principalName) {
        OAuth2AuthorizedClient oAuth2AuthorizedClient =
                oAuth2AuthorizedClientService.loadAuthorizedClient(AuthProvider.spotify.name(), principalName);
        spotifyApi.setAccessToken(oAuth2AuthorizedClient.getAccessToken().getTokenValue());
        PagingObject<SimplifiedPlaylistObject> pagingObject = spotifyApi.getUserPlaylists();
        List<SimplifiedPlaylistObject> simplifiedPlaylistObjectList = pagingObject.getItems();

        // if more playlists exists, then fetch next playlists
        while (pagingObject.getNext() != null) {
            pagingObject = spotifyApi.getUserPlaylists(pagingObject.getNext());
            simplifiedPlaylistObjectList.addAll(pagingObject.getItems());
        }

        return simplifiedPlaylistObjectList.stream()
                .map(simplifiedPlaylistObject -> new SimplePlaylistDomain(simplifiedPlaylistObject.getId(),
                        simplifiedPlaylistObject.getName()))
                .collect(Collectors.toList());
    }


}
