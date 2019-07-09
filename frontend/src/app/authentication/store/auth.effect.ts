import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {UserApiService} from '../../user/user-api.service';
import {of} from 'rxjs';
import {loadedUser, loadedUserFail, loggedIn} from './auth.actions';

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
        catchError(error => of(loadedUserFail({error: error})))
      ))
  ));

}
