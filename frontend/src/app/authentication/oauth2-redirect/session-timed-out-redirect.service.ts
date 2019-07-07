import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SessionTimedOutRedirectService {

  constructor() {
  }

  redirectLogin() {
    window.location.href = environment.apiUrl + '/oauth2/authorize/spotify?redirect_uri=' + environment.oauthRedirect;
  }
}
