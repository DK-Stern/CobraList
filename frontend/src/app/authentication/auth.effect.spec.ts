import {AuthEffects} from './auth.effect';
import {Observable, of} from 'rxjs';
import {TestBed} from '@angular/core/testing';
import {provideMockActions} from '@ngrx/effects/testing';
import {UserApiService} from './user-api.service';
import {loadedUser, loggedIn} from './auth.actions';
import {addMatchers, cold, getTestScheduler, hot, initTestScheduler, resetTestScheduler} from 'jasmine-marbles';
import {UserValueObject} from './user.value.object';

describe('AuthEffects', () => {

  let testSubject: AuthEffects;
  let actions: Observable<any>;
  let user: UserValueObject;

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

    const userApiServiceSpy = jasmine.createSpyObj('UserApiService', ['getUser']);
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
    const loggedInAction = loggedIn({token: '123'});
    const loadedUserAction = loadedUser({user: user});
    actions = hot('--a-', {a: loggedInAction});
    const expected = cold('--b', {b: loadedUserAction});

    // then
    expect(testSubject.loadUser$).toBeObservable(expected);
  });

});
