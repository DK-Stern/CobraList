import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {BasePlaylistObject, BasePlaylistsResponse} from './base-playlists.object';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../../environments/environment';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BasePlaylistService {

  constructor(private httpClient: HttpClient) {
  }

  getBasePlaylists(): Observable<BasePlaylistObject[]> {
    return this.httpClient.get<BasePlaylistsResponse>(environment.apiUrl + '/api/user/playlists')
      .pipe(
        map(response => {
          return response.playlists;
        })
      );
  }
}
