package sh.stern.cobralist.streaming.api;

import sh.stern.cobralist.streaming.domain.SimplePlaylistDomain;

import java.util.List;

public interface UsersPlaylistsService {
    List<SimplePlaylistDomain> getUsersPlaylists(String userName);
}
