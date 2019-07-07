export interface BasePlaylistObject {
  playlistId: string,
  playlistName: string
}

export interface BasePlaylistsResponse {
  playlists: BasePlaylistObject[];
}
