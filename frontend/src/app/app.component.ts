import {Component, OnInit} from '@angular/core';
import {environment} from '../environments/environment';
import {select, Store} from '@ngrx/store';
import {AppState} from './storage/appStateReducer';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  constructor(private store: Store<AppState>) {
  }

  title = 'CobraList';
  spotify_auth_url = environment.apiUrl + '/oauth2/authorize/spotify?redirect_uri=' + environment.oauthRedirect;
  username: string;

  ngOnInit() {
    this.store.pipe(select(state => state.authState))
      .subscribe(authState => authState.user
        ? this.username = authState.user.name
        : this.username = '');
  }
}
