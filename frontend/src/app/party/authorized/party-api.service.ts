import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {PartyDto} from './party.dto';

@Injectable({
  providedIn: 'root'
})
export class PartyApiService {

  constructor(private httpClient: HttpClient) {
  }

  getParty(partyId: string): Observable<PartyDto> {
    return this.httpClient.get<PartyDto>(environment.apiUrl + '/api/party/' + partyId);
  }
}
