package sh.stern.cobralist.streaming.api;

import sh.stern.cobralist.party.creation.domain.PlaylistDTO;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.streaming.domain.SimplePlaylistDTO;

import java.util.List;

public interface PlaylistService {

    List<SimplePlaylistDTO> getUsersPlaylists(String userName);

    PlaylistDTO createPlaylist(String userName, String streamingProviderUserId, String name, String description);

    String addTracksToPlaylist(String userName, String playlistId, List<TrackDTO> tracks);

    String addTracksWithPositionToPlaylist(String userName, String playlistId, List<TrackDTO> tracks, int position);

    List<TrackDTO> getTracksFromPlaylist(String userName, String playlistId);

    String moveTrackPosition(String userName, String playlistId, int oldPosition, int newPosition);

    void deleteParty(String username, String playlistId);
}
