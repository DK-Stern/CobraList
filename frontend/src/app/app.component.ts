import {Component, OnInit} from '@angular/core';
import {environment} from '../environments/environment';
import {select, Store} from '@ngrx/store';
import {AppState} from './storage/app-state.reducer';
import {LocalStorageService, STORAGE_KEY} from './storage/local-storage.service';
import {loggedIn} from './authentication/store/auth.actions';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  constructor(private store: Store<AppState>, private localStorageService: LocalStorageService) {
  }

  title = 'CobraList';
  spotify_auth_url = environment.apiUrl + '/oauth2/authorize/spotify?redirect_uri=' + environment.oauthRedirect;
  username: string = '';

  ngOnInit() {

    // load authToken from localStorage and dispatch it to redux store, if no authToken is present in redux store
    this.store.pipe(select(state => state.authentication.token))
      .subscribe(tokenState => {
        if (tokenState === null) {
          let token = <string>this.localStorageService.loadItem(STORAGE_KEY.TOKEN);
          if (token) {
            this.store.dispatch(loggedIn({token: token}));
          }
        }
      });

    this.store.select(state => state.authentication.user).subscribe(user =>
      user !== null
        ? this.username = user.name
        : this.username = '');
  }
}
