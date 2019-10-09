export interface AddMusicRequestDto {
  partyCode: string,
  track: {
    streamingId: string,
    artists: string[],
    uri: string,
    name: string,
    albumName: string,
    imageUrl: string,
    imageWidth: number,
    imageHeight: number,
    duration: number
  }
}
