import { Component, OnInit } from '@angular/core';
import { environment } from '../environments/environment';
import { select, Store } from '@ngrx/store';
import { AppState } from './storage/app-state.reducer';
import {
  LocalStorageService,
  STORAGE_KEY,
} from './storage/local-storage.service';
import {
  loginGuestSuccess,
  loginSuccess,
} from './authentication/store/auth.actions';
import { MediaMatcher } from '@angular/cdk/layout';
import { MatIconRegistry } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';
import { UserDto } from './user/user.dto';
import { UserRoles } from './user/user.roles';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  navElements = Array.of({ name: 'Start', navigate: 'home' });

  mobileQuery: MediaQueryList;
  title = 'CobraList';
  spotifyAuthUrl =
    environment.apiUrl +
    '/oauth2/authorize/spotify?redirect_uri=' +
    environment.oauthRedirect;
  username = '';
  hasDeleteElement = (el) => el.name === 'Lösche Party';
  hasLogoutElement = (el) => el.name === 'Logout';

  constructor(
    private store: Store<AppState>,
    private localStorageService: LocalStorageService,
    private media: MediaMatcher,
    iconRegistry: MatIconRegistry,
    sanitizer: DomSanitizer
  ) {
    iconRegistry.addSvgIcon(
      'menu',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/menu.svg')
    );
  }

  ngOnInit() {
    this.mobileQuery = this.media.matchMedia('(max-width: 600px)');
    // load authToken from localStorage and dispatch it to redux store, if no authToken is present in redux store
    this.store
      .pipe(select((state) => state.authentication.token))
      .subscribe((tokenState) => {
        if (tokenState === null) {
          const token = this.localStorageService.loadItem(
            STORAGE_KEY.TOKEN
          ) as string;
          const user = this.localStorageService.loadItem(
            STORAGE_KEY.USER
          ) as UserDto;
          if (token !== null && user !== null) {
            if (user.authorities.includes(UserRoles.USER)) {
              this.store.dispatch(loginSuccess({ token }));
            } else if (user.authorities.includes(UserRoles.GUEST)) {
              this.store.dispatch(
                loginGuestSuccess({ token: String(token), guest: user })
              );
            }
          }
        }
      });

    const defaultMenuElements = Array.of(
      { name: 'Start', navigate: 'home' },
      { name: 'Dashboard', navigate: 'user' },
      { name: 'Party erstellen', navigate: 'user/party/create' }
    );
    this.store
      .select((state) => state)
      .subscribe((state) => {
        const partyCode = state.party.partyCode;
        const isGuest = state.authentication.isGuest;
        this.username =
          state.authentication.user != null
            ? state.authentication.user.name
            : '';

        if (isGuest) {
          this.navElements = Array.of(
            { name: 'Start', navigate: 'home' },
            { name: 'Logout', navigate: 'logout' }
          );
        } else if (
          partyCode != null &&
          !isGuest &&
          state.authentication.user != null &&
          !this.navElements.some(this.hasDeleteElement)
        ) {
          // benutzer menu
          this.navElements.push({ name: 'Lösche Party', navigate: 'delete' });
          this.navElements.push({ name: 'Logout', navigate: 'logout' });
        } else {
          this.navElements = defaultMenuElements;
        }
      });
  }
}
