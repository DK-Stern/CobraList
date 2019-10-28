import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class DeletePartyService {

  constructor(private httpClient: HttpClient) {
  }

  deleteParty(partyCode: string) {
    return this.httpClient.delete(environment.apiUrl + '/api/party/delete/' + partyCode);
  }
}
