package sh.stern.cobralist.user.response;

import sh.stern.cobralist.api.domains.SimplePlaylistDomain;

import java.util.List;

public class BasePlaylistsResponse {
    private List<SimplePlaylistDomain> playlists;

    public BasePlaylistsResponse(List<SimplePlaylistDomain> playlists) {
        this.playlists = playlists;
    }

    public List<SimplePlaylistDomain> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<SimplePlaylistDomain> playlists) {
        this.playlists = playlists;
    }
}
