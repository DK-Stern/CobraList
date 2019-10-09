export interface PartyInformationDto {
  partyCode: string | null,
  downVotable: boolean | null,
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
  musicRequestId: number
  allVotes: number
  position: number
  alreadyVoted: boolean
  artist: string[]
  downVotes: number
  duration: number
  imageHeight: number
  imageUrl: string
  imageWidth: number
  rating: number
  title: string
  upVotes: number
}
