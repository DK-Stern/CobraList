import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MusicRequestVotingServiceService {

  url = environment.apiUrl + '/api/musicrequest/vote';

  constructor(private httpClient: HttpClient) {
  }

  voteMusicRequest(musicRequestId: number, isDownVote: boolean, partyCode: string) {
    this.httpClient.post(this.url, {
      downVote: isDownVote,
      musicRequestId: musicRequestId,
      partyCode: partyCode
    }).subscribe({error: e => console.log(e)});
  }
}
