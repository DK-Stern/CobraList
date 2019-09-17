export interface PartyInformationDto {
  currentPlayback: CurrentPlaybackDTO | null,
  musicRequests: MusicRequestDTO[] | null
}

export interface CurrentPlaybackDTO {
  currentTrack: CurrentTrackDTO
  playing: boolean
  progressMs: number
}

export interface CurrentTrackDTO {
  albumName: string
  artists: string[]
  duration: number
  id: string
  imageHeight: number
  imageUrl: string
  imageWidth: number
  name: string
  uri: string
}

export interface MusicRequestDTO {
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
