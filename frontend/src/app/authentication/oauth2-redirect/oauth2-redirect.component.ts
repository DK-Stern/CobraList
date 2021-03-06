import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {loginSuccess} from '../store/auth.actions';
import {AppState} from '../../storage/app-state.reducer';
import {MatIconRegistry} from '@angular/material';
import {DomSanitizer} from '@angular/platform-browser';
import {LocalStorageService, STORAGE_KEY} from '../../storage/local-storage.service';
import {UserDto} from '../../user/user.dto';

@Component({
  selector: 'app-oauth2-redirect',
  templateUrl: './oauth2-redirect.component.html',
  styleUrls: ['./oauth2-redirect.component.scss']
})
export class Oauth2RedirectComponent implements OnInit {

  token: string;
  error: string;

  user$ = this.store.select(state => state.authentication.user);
  user: UserDto = null;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private store: Store<AppState>,
              private iconRegistry: MatIconRegistry,
              private sanitizer: DomSanitizer,
              private localStorageService: LocalStorageService) {
    iconRegistry.addSvgIcon(
      'thumbs-up',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/examples/thumbup-icon.svg'));
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.token = params["token"];
      this.error = params["error"];

      if (this.token != null) {
        this.localStorageService.saveItem(STORAGE_KEY.TOKEN, this.token);
        this.store.dispatch(loginSuccess({token: this.token}));
      }

      this.user$.subscribe(user => {
        if (user) {
          this.router.navigateByUrl('user');
        }
      });
    })
  }
}
