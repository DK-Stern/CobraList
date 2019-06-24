import {Component, OnInit} from '@angular/core';
import {environment} from '../environments/environment';
import {select, Store} from '@ngrx/store';
import {AppState} from './storage/appStateReducer';
import {LocalStorageService, STORAGE_KEY} from './storage/local-storage.service';
import {loadedUser} from './authentication/auth.actions';
import {UserValueObject} from './authentication/user.value.object';

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
    this.store.pipe(select(state => state.authState))
      .subscribe(authState => {
        if (authState.user) {
          this.username = authState.user.name;
        } else {
          let user = <UserValueObject>this.localStorageService.loadItem(STORAGE_KEY.USER);
          if (user) {
            this.username = user.name;
            this.store.dispatch(loadedUser({user: user}));
          }
        }
      });
  }
}
