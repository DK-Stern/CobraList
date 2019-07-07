package sh.stern.cobralist.api.interfaces;

import sh.stern.cobralist.api.domains.SimplePlaylistDomain;

import java.util.List;

public interface UsersPlaylistsService {
    List<SimplePlaylistDomain> getUsersPlaylists(String principalName);
}
