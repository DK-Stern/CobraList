export interface SearchMusicRequestDto {
  alreadyInPlaylist: boolean,
  trackId: string,
  artists: string[],
  uri: string,
  name: string,
  albumName: string,
  imageUrl: string,
  imageWidth: number,
  imageHeight: number,
  duration: number
}
