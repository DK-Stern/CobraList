import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {UserApiService} from '../../user/user-api.service';
import {of} from 'rxjs';
import {loadedUser, loadedUserFail, loggedIn} from './auth.actions';
import {LocalStorageService, STORAGE_KEY} from '../../storage/local-storage.service';

@Injectable()
export class AuthEffects {

  constructor(private actions$: Actions,
              private userApiService: UserApiService,
              private localStorageService: LocalStorageService) {
  }

  loadUser$ = createEffect(() => this.actions$.pipe(
    ofType(loggedIn),
    mergeMap(() => this.userApiService.getUser()
      .pipe(
        map(user => loadedUser({user: user})),
        catchError(error => {
          this.localStorageService.saveItem(STORAGE_KEY.TOKEN, null);
          return of(loadedUserFail({error: error}));
        })
      ))
  ));

}
