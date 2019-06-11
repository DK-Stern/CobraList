import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {UserValueObject} from '../UserValueObject';
import {Store} from '@ngrx/store';
import {loggedIn} from '../auth.actions';
import {AppState} from '../../storage/appStateReducer';
import {Observable} from 'rxjs';
import * as auth from '../auth.reducers';

@Component({
  selector: 'app-oauth2-redirect',
  templateUrl: './oauth2-redirect.component.html',
  styleUrls: ['./oauth2-redirect.component.scss']
})
export class Oauth2RedirectComponent implements OnInit {


  authState$: Observable<auth.State> = this.store.select(state => state.authState);

  token: string;
  user: UserValueObject;
  error: string;


  constructor(private route: ActivatedRoute,
              private store: Store<AppState>) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.token = params["token"];
      this.error = params["error"];

      if (this.token != null) {
        this.store.dispatch(loggedIn({token: this.token}));
      }

      this.authState$.subscribe(auth => {
        this.user = auth.user;
      })
    })
  }
}
