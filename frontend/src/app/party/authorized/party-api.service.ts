import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {PartyInformationDto} from "./store/party-information.dto";

@Injectable({
  providedIn: 'root'
})
export class PartyApiService {

  constructor(private httpClient: HttpClient) {
  }

  getPartyInformation(partyCode: string): Observable<PartyInformationDto> {
    return this.httpClient.get<PartyInformationDto>(environment.apiUrl + '/api/party/' + partyCode);
  }
}
