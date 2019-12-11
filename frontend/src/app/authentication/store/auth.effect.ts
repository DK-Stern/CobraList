import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {UserApiService} from '../../user/user-api.service';
import {of} from 'rxjs';
import {loadedUser, loadedUserFail, loginSuccess} from './auth.actions';
import {LocalStorageService, STORAGE_KEY} from '../../storage/local-storage.service';
import {UserDto} from "../../user/user.dto";
import {UserRoles} from "../../user/user.roles";

@Injectable()
export class AuthEffects {

  constructor(private actions$: Actions,
              private userApiService: UserApiService,
              private localStorageService: LocalStorageService) {
  }

  loadUser$ = createEffect(() => this.actions$.pipe(
    ofType(loginSuccess),
    mergeMap(() => this.userApiService.getUser()
      .pipe(
        map(user => {
          user.authorities = [UserRoles.USER];
          this.localStorageService.saveItem(STORAGE_KEY.USER, user);
          return loadedUser({user: user})}),
        catchError(error => {
          this.localStorageService.clearAll();
          return of(loadedUserFail({error: error}));
        })
      ))
  ));

}
