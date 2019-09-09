package sh.stern.cobralist.user.controller;

import sh.stern.cobralist.streaming.domain.SimplePlaylistDTO;

import java.util.List;

public class BasePlaylistsResponse {
    private List<SimplePlaylistDTO> playlists;

    public BasePlaylistsResponse(List<SimplePlaylistDTO> playlists) {
        this.playlists = playlists;
    }

    public List<SimplePlaylistDTO> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<SimplePlaylistDTO> playlists) {
        this.playlists = playlists;
    }
}
