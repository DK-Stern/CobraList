import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {AuthService} from './auth.service';
import {EMPTY} from 'rxjs';
import {loadedUser, loggedIn} from './auth.actions';

@Injectable()
export class AuthEffects {

  constructor(private actions$: Actions,
              private authService: AuthService) {
  }

  loadUser$ = createEffect(() => this.actions$.pipe(
    ofType(loggedIn),
    mergeMap(() => this.authService.getUser()
      .pipe(
        map(user => loadedUser({user: user})),
        catchError(() => EMPTY)
      ))
  ));

}
