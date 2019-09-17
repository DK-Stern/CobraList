export interface PartyInformationDto {
  currentPlayback: CurrentPlaybackDTO | null,
  musicRequests: MusicRequestsDTO[] | null
}

export interface CurrentPlaybackDTO {
  currentTrack: {
    albumName: string
    artists: string[]
    duration: bigint
    id: string
    imageHeight: bigint
    imageUrl: string
    imageWidth: bigint
    name: string
    uri: string
  }
  playing: boolean
  progressMs: bigint
}

export interface MusicRequestsDTO {
  allVotes: bigint
  alreadyVoted: boolean
  artist: string[]
  downVotes: bigint
  duration: bigint
  imageHeight: bigint
  imageUrl: string
  imageWidth: bigint
  rating: bigint
  title: string
  trackId: string
  upVotes: bigint
}
