import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {environment} from '../environments/environment';
import {select, Store} from '@ngrx/store';
import {AppState} from './storage/app-state.reducer';
import {LocalStorageService, STORAGE_KEY} from './storage/local-storage.service';
import {loginSuccess} from './authentication/store/auth.actions';
import {MediaMatcher} from '@angular/cdk/layout';
import {MatIconRegistry} from '@angular/material';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  navElements = Array.of(
    {name: 'Start', navigate: 'home'},
    {name: 'Dashboard', navigate: 'user'},
    {name: 'Party erstellen', navigate: 'user/party/create'});

  mobileQuery: MediaQueryList;
  title = 'CobraList';
  spotify_auth_url = environment.apiUrl + '/oauth2/authorize/spotify?redirect_uri=' + environment.oauthRedirect;
  username: string = '';
  hasLogoutElement = (el) => el.name === 'Logout';

  private readonly _mobileQueryListener: () => void;

  constructor(private store: Store<AppState>,
              private localStorageService: LocalStorageService,
              changeDetectorRef: ChangeDetectorRef,
              media: MediaMatcher,
              iconRegistry: MatIconRegistry,
              sanitizer: DomSanitizer) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);

    iconRegistry.addSvgIcon(
      'menu',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/menu.svg'));
  }

  ngOnInit() {
    // load authToken from localStorage and dispatch it to redux store, if no authToken is present in redux store
    this.store.pipe(select(state => state.authentication.token))
      .subscribe(tokenState => {
        if (tokenState === null) {
          let token = <string>this.localStorageService.loadItem(STORAGE_KEY.TOKEN);
          if (token) {
            this.store.dispatch(loginSuccess({token: token}));
          }
        }
      });

    this.store.select(state => state.authentication.user).subscribe(user => {
      if (user !== null) {
        if (!this.navElements.some(this.hasLogoutElement)) {
          this.navElements.push({name: 'Logout', navigate: 'logout'});
        }
        this.username = user.name
      } else {
        if (this.navElements.some(this.hasLogoutElement)) {
          this.navElements.pop();
        }
        this.username = '';
      }
    });
  }
}
