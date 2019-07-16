import {inject, TestBed} from '@angular/core/testing';

import {UserAuthGuard} from './user-auth.guard';
import {MockStore, provideMockStore} from '@ngrx/store/testing';
import {AppState} from '../storage/app-state.reducer';
import {Store} from '@ngrx/store';
import {SessionTimedOutRedirectService} from './oauth2-redirect/session-timed-out-redirect.service';
import {RouterTestingModule} from '@angular/router/testing';

describe('UserAuthGuard', () => {

  let redirectLoginSpy;
  let testSubject: UserAuthGuard;
  let storageSpy: MockStore<AppState>;
  let initialState: AppState = {
    authentication: {
      isAuthenticated: false,
      token: null,
      user: null,
      error: null
    },
    party: null
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        UserAuthGuard,
        provideMockStore({initialState})
      ],
      imports: [
        RouterTestingModule
      ]
    });

    let sessionTimedOutRedirectService = TestBed.get(SessionTimedOutRedirectService);
    redirectLoginSpy = spyOn(sessionTimedOutRedirectService, 'redirectLogin');

    testSubject = TestBed.get(UserAuthGuard);
    storageSpy = TestBed.get(Store);
  });

  it('can activate route after AuthState is set to isAuthenticated "true"', inject([UserAuthGuard], (guard: UserAuthGuard) => {
    // given
    storageSpy.setState({
      authentication: {
        isAuthenticated: true,
        user: null,
        token: null,
        error: null
      },
      party: null
    });

    // when
    const resultedCanActivate = testSubject.canActivate(null, null);

    // then
    expect(resultedCanActivate).toBeTruthy();
  }));

  it('can not activate route after AuthState is set to isAuthenticated "false"', inject([UserAuthGuard], (guard: UserAuthGuard) => {
    // when
    const resultedCanActivate = testSubject.canActivate(null, null);

    // then
    expect(resultedCanActivate).toBeFalsy();
  }));

  it('can activate child route after AuthState is set to isAuthenticated "true"', inject([UserAuthGuard], (guard: UserAuthGuard) => {
    // given
    storageSpy.setState({
      authentication: {
        isAuthenticated: true,
        user: null,
        token: null,
        error: null
      },
      party: null
    });

    // when
    const resultedCanActivate = testSubject.canActivateChild(null, null);

    // then
    expect(resultedCanActivate).toBeTruthy();
  }));

  it('can not activate child route after AuthState is set to isAuthenticated "false"', inject([UserAuthGuard], (guard: UserAuthGuard) => {
    // when
    const resultedCanActivate = testSubject.canActivateChild(null, null);

    // then
    expect(resultedCanActivate).toBeFalsy();
  }));

  it('can loadItem route after AuthState is set to isAuthenticated "true"', inject([UserAuthGuard], (guard: UserAuthGuard) => {
    // given
    storageSpy.setState({
      authentication: {
        isAuthenticated: true,
        user: null,
        token: null,
        error: null
      },
      party: null
    });

    // when
    const resultedCanActivate = testSubject.canLoad(null, null);

    // then
    expect(resultedCanActivate).toBeTruthy();
  }));

  it('can not loadItem route after AuthState is set to isAuthenticated "false"', inject([UserAuthGuard], (guard: UserAuthGuard) => {
    // when
    const resultedCanActivate = testSubject.canLoad(null, null);

    // then
    expect(resultedCanActivate).toBeFalsy();
  }));
});
