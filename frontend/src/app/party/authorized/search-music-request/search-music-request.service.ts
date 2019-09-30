import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";
import {SearchMusicRequestDto} from "./search.music-request.dto";
import {AddMusicRequestDto} from "./add.music-request.dto";

@Injectable({
  providedIn: 'root'
})
export class SearchMusicRequestService {

  url = environment.apiUrl + '/api/musicrequest/';

  constructor(private httpClient: HttpClient) {
  }

  searchMusicRequest(partyCode: string, searchString: string): Observable<SearchMusicRequestDto[]> {
    return this.httpClient.post<SearchMusicRequestDto[]>(this.url + 'search', {
      partyCode: partyCode,
      searchString: searchString
    });
  }

  addMusicRequest(addMusicRequestDto: AddMusicRequestDto) {
    return this.httpClient.post(this.url + 'add', addMusicRequestDto);
  }
}
