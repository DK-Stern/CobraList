import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {FindPartyDto} from './find-party.dto';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FindPartyService {

  constructor(private httpClient: HttpClient) {
  }

  findParty(id: string): Observable<FindPartyDto> {
    return this.httpClient.get<FindPartyDto>(environment.apiUrl + '/api/party/find/' + id);
  }
}
