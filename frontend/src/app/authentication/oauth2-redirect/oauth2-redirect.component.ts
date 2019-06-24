import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {loggedIn} from '../auth.actions';
import {AppState} from '../../storage/appStateReducer';
import {Observable} from 'rxjs';
import * as auth from '../auth.reducers';
import {MatIconRegistry} from '@angular/material';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-oauth2-redirect',
  templateUrl: './oauth2-redirect.component.html',
  styleUrls: ['./oauth2-redirect.component.scss']
})
export class Oauth2RedirectComponent implements OnInit {


  authState$: Observable<auth.State> = this.store.select(state => state.authState);

  token: string;
  error: string;


  constructor(private route: ActivatedRoute,
              private router: Router,
              private store: Store<AppState>,
              private iconRegistry: MatIconRegistry,
              private sanitizer: DomSanitizer) {
    iconRegistry.addSvgIcon(
      'thumbs-up',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/examples/thumbup-icon.svg'));
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.token = params["token"];
      this.error = params["error"];

      if (this.token != null) {
        this.store.dispatch(loggedIn({token: this.token}));
      }

      this.authState$.subscribe(auth => {
        this.router.navigateByUrl('user');
      })
    })
  }
}
