import {Injectable} from '@angular/core';
import {UserDto} from './user.dto';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserApiService {

  constructor(private http: HttpClient) {
  }

  getUser(): Observable<UserDto> {
    return this.http.get<UserDto>(environment.apiUrl + '/api/user/me');
  }
}
