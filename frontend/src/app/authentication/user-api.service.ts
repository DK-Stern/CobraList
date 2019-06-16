import {Injectable} from '@angular/core';
import {UserValueObject} from './user.value.object';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserApiService {

  constructor(private http: HttpClient) {
  }

  getUser(): Observable<UserValueObject> {
    return this.http.get<UserValueObject>(environment.apiUrl + '/api/user/me');
  }
}
