import {TrackDto} from "./track.dto";

export interface AddMusicRequestDto {
  partyCode: string,
  trackDTO: TrackDto
}
