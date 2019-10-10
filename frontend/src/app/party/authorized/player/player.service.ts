import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  url = environment.apiUrl + '/api/party/player';

  constructor(private httpClient: HttpClient) {
  }

  startParty(partyCode: string) {
    this.httpClient.put(this.url + "/start/" + partyCode, null).subscribe({error: err => console.log(err)})
  }

  stopParty(partyCode: string) {
    this.httpClient.put(this.url + "/stop/" + partyCode, null).subscribe({error: err => console.log(err)})
  }

  skipParty(partyCode: string) {
    this.httpClient.put(this.url + "/skip/" + partyCode, null).subscribe({error: err => console.log(err)})
  }
}
