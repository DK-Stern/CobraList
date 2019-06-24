import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {UserApiService} from './user-api.service';
import {EMPTY, of} from 'rxjs';
import {loadedUser, loadedUserFail, loggedIn} from './auth.actions';
import {LocalStorageService, STORAGE_KEY} from '../storage/local-storage.service';

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
        map(user => {
          this.localStorageService.saveItem(STORAGE_KEY.USER, user);
          return loadedUser({user: user})
        }),
        catchError(error => of(loadedUserFail({error: error})))
      ))
  ));

}
