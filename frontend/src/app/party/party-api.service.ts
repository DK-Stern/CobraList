import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {PartyCreationValueObject} from '../user/create-party/party-creation-value.object';

@Injectable({
  providedIn: 'root'
})
export class PartyApiService {

  constructor(private httpClient: HttpClient) {
  }

  getParty(partyId: string): Observable<PartyCreationValueObject> {
    return this.httpClient.get<PartyCreationValueObject>(environment.apiUrl + '/api/party/' + partyId);
  }
}
