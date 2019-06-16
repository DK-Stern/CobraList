import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {UserApiService} from './user-api.service';
import {EMPTY} from 'rxjs';
import {loadedUser, loggedIn} from './auth.actions';

@Injectable()
export class AuthEffects {

  constructor(private actions$: Actions,
              private userApiService: UserApiService) {
  }

  loadUser$ = createEffect(() => this.actions$.pipe(
    ofType(loggedIn),
    mergeMap(() => this.userApiService.getUser()
      .pipe(
        map(user => loadedUser({user: user})),
        catchError(() => EMPTY)
      ))
  ));

}
