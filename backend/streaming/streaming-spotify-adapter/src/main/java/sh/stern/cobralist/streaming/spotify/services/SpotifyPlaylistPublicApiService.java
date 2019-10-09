package sh.stern.cobralist.streaming.spotify.services;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.creation.domain.PlaylistDTO;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.streaming.api.PlaylistService;
import sh.stern.cobralist.streaming.domain.SimplePlaylistDTO;
import sh.stern.cobralist.streaming.exceptions.AccessTokenExpiredException;
import sh.stern.cobralist.streaming.spotify.SpotifyApi;
import sh.stern.cobralist.streaming.spotify.mapper.TrackValueObjectToTrackDTOMapper;
import sh.stern.cobralist.streaming.spotify.valueobjects.PagingObject;
import sh.stern.cobralist.streaming.spotify.valueobjects.PlaylistObject;
import sh.stern.cobralist.streaming.spotify.valueobjects.SimplifiedPlaylistObject;
import sh.stern.cobralist.streaming.spotify.valueobjects.TrackValueObjectWrapper;
import sh.stern.cobralist.user.domain.StreamingProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotifyPlaylistPublicApiService implements PlaylistService {

    private static final String API_URL = "https://api.spotify.com/v1/";
    private static final int LIST_SIZE_FOR_ADDING_TRACKS = 100;
    public static final String PLAYLIST_TRACKS_API_URL = "%splaylists/%s/tracks";

    private final SpotifyApi spotifyApi;
    private final TrackValueObjectToTrackDTOMapper trackValueObjectToTrackDTOMapper;

    @Autowired
    public SpotifyPlaylistPublicApiService(SpotifyApi spotifyApi,
                                           TrackValueObjectToTrackDTOMapper trackValueObjectToTrackDTOMapper) {
        this.spotifyApi = spotifyApi;
        this.trackValueObjectToTrackDTOMapper = trackValueObjectToTrackDTOMapper;
    }

    @Override
    public List<SimplePlaylistDTO> getUsersPlaylists(String userName) {
        spotifyApi.setAuthentication(StreamingProvider.spotify, userName);

        PagingObject<SimplifiedPlaylistObject> pagingObject = getUserPlaylistFromSpotify(userName, String.format("%sme/playlists", API_URL));
        List<SimplifiedPlaylistObject> simplifiedPlaylistObjectList = pagingObject.getItems();

        // if more playlists exists, then fetch next playlists
        while (pagingObject.getNext() != null) {
            pagingObject = getUserPlaylistFromSpotify(userName, pagingObject.getNext());
            simplifiedPlaylistObjectList.addAll(pagingObject.getItems());
        }

        return simplifiedPlaylistObjectList.stream()
                .map(simplifiedPlaylistObject -> new SimplePlaylistDTO(simplifiedPlaylistObject.getId(),
                        simplifiedPlaylistObject.getName()))
                .collect(Collectors.toList());
    }

    private PagingObject<SimplifiedPlaylistObject> getUserPlaylistFromSpotify(String userName, String url) {
        try {
            return spotifyApi.getUserPlaylists(url);
        } catch (AccessTokenExpiredException expired) {
            spotifyApi.setAuthentication(StreamingProvider.spotify, userName);
            return getUserPlaylistFromSpotify(userName, url);
        }
    }

    @Override
    public PlaylistDTO createPlaylist(String userName, String streamingProviderUserId, String name, String description) {
        spotifyApi.setAuthentication(StreamingProvider.spotify, userName);

        PlaylistObject playlistObject = createPlaylistOnSpotify(userName, String.format("%susers/%s/playlists", API_URL, streamingProviderUserId), name, description);

        final PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setPlaylistId(playlistObject.getId());
        playlistDTO.setName(playlistObject.getName());

        return playlistDTO;
    }

    private PlaylistObject createPlaylistOnSpotify(String userName, String url, String name, String description) {
        try {
            return spotifyApi.createPlaylist(url, name, description);
        } catch (AccessTokenExpiredException e) {
            spotifyApi.setAuthentication(StreamingProvider.spotify, userName);
            return createPlaylistOnSpotify(userName, url, name, description);
        }
    }

    @Override
    public String addTracksToPlaylist(String userName, String playlistId, List<TrackDTO> tracks) {
        final List<String> uriList = getUrisFromTrackList(tracks);
        final List<List<String>> splittedUriList = splitList(uriList);

        spotifyApi.setAuthentication(StreamingProvider.spotify, userName);

        final String url = String.format(PLAYLIST_TRACKS_API_URL, API_URL, playlistId);
        final List<String> snapshots = splittedUriList.stream()
                .map((List<String> uris) -> postTracksToPlaylist(userName, url, uris))
                .collect(Collectors.toList());

        return snapshots.get(snapshots.size() - 1);
    }

    private List<String> getUrisFromTrackList(List<TrackDTO> tracks) {
        return tracks.stream()
                .map(TrackDTO::getUri)
                .collect(Collectors.toList());
    }

    List<List<String>> splitList(List<String> uriList) {
        return ListUtils.partition(uriList, LIST_SIZE_FOR_ADDING_TRACKS);
    }

    private String postTracksToPlaylist(String username, String url, List<String> trackUris) {
        try {
            return spotifyApi.postTracksToPlaylist(url, trackUris);
        } catch (AccessTokenExpiredException e) {
            spotifyApi.setAuthentication(StreamingProvider.spotify, username);
            return postTracksToPlaylist(username, url, trackUris);
        }
    }

    @Override
    public String addTracksWithPositionToPlaylist(String userName, String playlistId, List<TrackDTO> tracks, int position) {
        final List<String> uriList = getUrisFromTrackList(tracks);
        final List<List<String>> splittedUriList = splitList(uriList);

        spotifyApi.setAuthentication(StreamingProvider.spotify, userName);

        final String url = String.format(PLAYLIST_TRACKS_API_URL, API_URL, playlistId);
        final List<String> snapshots = splittedUriList.stream()
                .map((List<String> uris) -> postTracksWithPositionToPlaylist(userName, url, uris, position))
                .collect(Collectors.toList());

        return snapshots.get(snapshots.size() - 1);
    }

    private String postTracksWithPositionToPlaylist(String username, String url, List<String> trackUris, int position) {
        try {
            return spotifyApi.postTracksWithPositionToPlaylist(url, trackUris, position);
        } catch (AccessTokenExpiredException e) {
            spotifyApi.setAuthentication(StreamingProvider.spotify, username);
            return postTracksWithPositionToPlaylist(username, url, trackUris, position);
        }
    }

    @Override
    public List<TrackDTO> getTracksFromPlaylist(String userName, String playlistId) {
        spotifyApi.setAuthentication(StreamingProvider.spotify, userName);

        final String playlistUrl = String.format("%splaylists/%s/tracks?fields=items(track(id,is_local,duration_ms,uri,name,artists,album(name,images))),next,href,previous,limit,offset,total", API_URL, playlistId);
        PagingObject<TrackValueObjectWrapper> pagingObject = getMusicTracksFromPlaylist(userName, playlistUrl);
        final List<TrackValueObjectWrapper> tracks = new ArrayList<>(pagingObject.getItems());

        while (pagingObject.getNext() != null) {
            pagingObject = getMusicTracksFromPlaylist(userName, pagingObject.getNext());
            tracks.addAll(pagingObject.getItems());
        }

        return tracks.stream()
                .filter(wrapper -> !wrapper.getTrack().getLocal())
                .map(TrackValueObjectWrapper::getTrack)
                .map(trackValueObjectToTrackDTOMapper::map)
                .collect(Collectors.toList());
    }

    private PagingObject<TrackValueObjectWrapper> getMusicTracksFromPlaylist(String userName, String url) {
        try {
            return spotifyApi.getTracksFromPlaylist(url);
        } catch (AccessTokenExpiredException e) {
            spotifyApi.setAuthentication(StreamingProvider.spotify, userName);
            return getMusicTracksFromPlaylist(userName, url);
        }
    }

    @Override
    public String moveTrackPosition(String userName, String playlistId, int oldPosition, int newPosition) {
        final String url = String.format(PLAYLIST_TRACKS_API_URL, API_URL, playlistId);
        spotifyApi.setAuthentication(StreamingProvider.spotify, userName);
        return reorderTrackPosition(userName, url, oldPosition, newPosition);
    }

    private String reorderTrackPosition(String username, String url, int oldPosition, int newPosition) {
        try {
            return spotifyApi.reorderTrack(url, oldPosition, newPosition);
        } catch (AccessTokenExpiredException e) {
            spotifyApi.setAuthentication(StreamingProvider.spotify, username);
            return reorderTrackPosition(username, url, oldPosition, newPosition);
        }
    }
}
