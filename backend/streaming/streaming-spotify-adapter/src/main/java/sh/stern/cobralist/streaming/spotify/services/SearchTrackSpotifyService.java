package sh.stern.cobralist.streaming.spotify.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.streaming.api.SearchTrackStreamingService;
import sh.stern.cobralist.streaming.exceptions.AccessTokenExpiredException;
import sh.stern.cobralist.streaming.spotify.SpotifyApi;
import sh.stern.cobralist.streaming.spotify.mapper.TrackValueObjectToTrackDTOMapper;
import sh.stern.cobralist.streaming.spotify.valueobjects.SearchTrackValueObject;
import sh.stern.cobralist.user.domain.StreamingProvider;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchTrackSpotifyService implements SearchTrackStreamingService {

    private static final String API_SEARCH_URL = "https://api.spotify.com/v1/search";
    private final SpotifyApi spotifyApi;
    private final TrackValueObjectToTrackDTOMapper trackValueObjectToTrackDTOMapper;

    @Autowired
    public SearchTrackSpotifyService(SpotifyApi spotifyApi,
                                     TrackValueObjectToTrackDTOMapper trackValueObjectToTrackDTOMapper) {
        this.spotifyApi = spotifyApi;
        this.trackValueObjectToTrackDTOMapper = trackValueObjectToTrackDTOMapper;
    }

    @Override
    public List<TrackDTO> searchTrack(String username, String searchString) {
        final String url = API_SEARCH_URL + "?q=" + URLEncoder.encode(searchString, StandardCharsets.UTF_8) + "&type=track";

        final SearchTrackValueObject resultedTracksValueObject = searchSpotifyTrack(username, url);

        return resultedTracksValueObject.getItems().stream()
                .filter(track -> !track.getLocal())
                .map(trackValueObjectToTrackDTOMapper::map)
                .collect(Collectors.toList());
    }

    private SearchTrackValueObject searchSpotifyTrack(String username, String url) {
        try {
            spotifyApi.setAuthentication(StreamingProvider.spotify, username);
            return spotifyApi.searchTrack(url);
        } catch (AccessTokenExpiredException e) {
            return searchSpotifyTrack(username, url);
        }
    }
}
