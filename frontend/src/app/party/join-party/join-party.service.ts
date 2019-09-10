import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {JoinPartyDto} from './join-party.dto';
import {environment} from '../../../environments/environment';
import {Observable} from "rxjs";

interface PartyJoinedDto {
  token: string,
  partyCode: string
}

@Injectable({
  providedIn: 'root'
})
export class JoinPartyService {

  constructor(private httpClient: HttpClient) {
  }

  joinParty(joinPartyDto: JoinPartyDto): Observable<PartyJoinedDto> {
    return this.httpClient.post<PartyJoinedDto>(environment.apiUrl + '/api/party/join/', joinPartyDto);
  }
}
