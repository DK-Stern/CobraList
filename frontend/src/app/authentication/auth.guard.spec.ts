import {inject, TestBed} from '@angular/core/testing';

import {AuthGuard} from './auth.guard';
import {MockStore, provideMockStore} from '@ngrx/store/testing';
import {AppState} from '../storage/appStateReducer';
import {Store} from '@ngrx/store';

describe('AuthGuard', () => {

  let testSubject: AuthGuard;
  let storageSpy: MockStore<AppState>;
  let initialState: AppState = {
    authState: {
      isAuthenticated: false,
      token: null,
      user: null,
      error: null
    }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        provideMockStore({initialState})
      ]
    });

    testSubject = TestBed.get(AuthGuard);
    storageSpy = TestBed.get(Store);
  });

  it('can activate route after authState is set to isAuthenticated "true"', inject([AuthGuard], (guard: AuthGuard) => {
    // given
    storageSpy.setState({
      authState: {
        isAuthenticated: true,
        user: null,
        token: null,
        error: null
      }
    });

    // when
    const resultedCanActivate = testSubject.canActivate(null, null);

    // then
    expect(resultedCanActivate).toBeTruthy();
  }));

  it('can not activate route after authState is set to isAuthenticated "false"', inject([AuthGuard], (guard: AuthGuard) => {
    // when
    const resultedCanActivate = testSubject.canActivate(null, null);

    // then
    expect(resultedCanActivate).toBeFalsy();
  }));

  it('can activate child route after authState is set to isAuthenticated "true"', inject([AuthGuard], (guard: AuthGuard) => {
    // given
    storageSpy.setState({
      authState: {
        isAuthenticated: true,
        user: null,
        token: null,
        error: null
      }
    });

    // when
    const resultedCanActivate = testSubject.canActivateChild(null, null);

    // then
    expect(resultedCanActivate).toBeTruthy();
  }));

  it('can not activate child route after authState is set to isAuthenticated "false"', inject([AuthGuard], (guard: AuthGuard) => {
    // when
    const resultedCanActivate = testSubject.canActivateChild(null, null);

    // then
    expect(resultedCanActivate).toBeFalsy();
  }));

  it('can loadItem route after authState is set to isAuthenticated "true"', inject([AuthGuard], (guard: AuthGuard) => {
    // given
    storageSpy.setState({
      authState: {
        isAuthenticated: true,
        user: null,
        token: null,
        error: null
      }
    });

    // when
    const resultedCanActivate = testSubject.canLoad(null, null);

    // then
    expect(resultedCanActivate).toBeTruthy();
  }));

  it('can not loadItem route after authState is set to isAuthenticated "false"', inject([AuthGuard], (guard: AuthGuard) => {
    // when
    const resultedCanActivate = testSubject.canLoad(null, null);

    // then
    expect(resultedCanActivate).toBeFalsy();
  }));
});
