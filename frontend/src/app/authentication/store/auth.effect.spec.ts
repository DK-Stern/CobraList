import {AuthEffects} from './auth.effect';
import {Observable, of} from 'rxjs';
import {TestBed} from '@angular/core/testing';
import {provideMockActions} from '@ngrx/effects/testing';
import {UserApiService} from '../../user/user-api.service';
import {loadedUser, loadedUserFail, loginSuccess} from './auth.actions';
import {addMatchers, cold, getTestScheduler, hot, initTestScheduler, resetTestScheduler} from 'jasmine-marbles';
import {UserValueObject} from '../../user/user.value.object';

describe('AuthEffects', () => {

  let testSubject: AuthEffects;
  let actions: Observable<any>;
  let user: UserValueObject;
  let userApiServiceSpy;

  // configure matchers for jasmine-marbles
  jasmine.getEnv().beforeAll(() => {
    return addMatchers();
  });
  jasmine.getEnv().beforeEach(() => {
    initTestScheduler();
  });
  jasmine.getEnv().afterEach(() => {
    getTestScheduler().flush();
    resetTestScheduler();
  });

  beforeEach(() => {
    user = {
      authorities: ['USER'],
      email: 'email@mail.de',
      id: 1,
      name: 'Max'
    };


    userApiServiceSpy = jasmine.createSpyObj('UserApiService', ['getUser']);
    userApiServiceSpy.getUser.and.returnValue(of(user));

    TestBed.configureTestingModule({
      providers: [
        AuthEffects,
        {
          provide: UserApiService,
          useValue: userApiServiceSpy
        },
        provideMockActions(() => actions)
      ]
    });

    testSubject = TestBed.get(AuthEffects);
  });

  it('should use user api service to get user object', () => {
    // given
    const loggedInAction = loginSuccess({token: '123'});
    const loadedUserAction = loadedUser({user: user});
    actions = hot('--a-', {a: loggedInAction});
    const expected = cold('--b', {b: loadedUserAction});

    // then
    expect(testSubject.loadUser$).toBeObservable(expected);
  });

  it('should return LoadUserFailAction if an error occures on saving user', () => {
    // given
    const loggedInAction = loginSuccess({token: '123'});
    const loadUserError = new Error('error') as any;
    const outcome = loadedUserFail({error: loadUserError});

    actions = hot('-a', {a: loggedInAction});
    const response = cold('-#|', {}, loadUserError);
    userApiServiceSpy.getUser.and.returnValue(response);

    const expected = cold('--b', {b: outcome});

    // when
    const loadUser$ = testSubject.loadUser$;

    // then
    expect(loadUser$).toBeObservable(expected);
  });
});
