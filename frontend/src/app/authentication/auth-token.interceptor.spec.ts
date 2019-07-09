import {UserApiService} from '../user/user-api.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {TestBed} from '@angular/core/testing';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {AuthTokenInterceptor} from './auth-token.interceptor';
import {environment} from '../../environments/environment';
import {UserValueObject} from '../user/user.value.object';
import {MockStore, provideMockStore} from '@ngrx/store/testing';
import {AppState} from '../storage/app-state.reducer';
import {MatSnackBarModule} from '@angular/material';

describe('AuthTokenInterceptor', () => {

  let userApiService: UserApiService;
  let httpMock: HttpTestingController;
  let store: MockStore<AppState>;
  const initialState = {
    authentication: {
      token: '123'
    }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatSnackBarModule
      ],
      providers: [
        UserApiService,
        provideMockStore({initialState}),
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AuthTokenInterceptor,
          multi: true
        }
      ]
    });

    userApiService = TestBed.get(UserApiService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should add an authorization header on http request', () => {
    // given
    const mockUser: UserValueObject = {
      id: 1,
      name: 'Max',
      email: 'max@email.de',
      authorities: ['USER']
    };

    // when
    userApiService.getUser().subscribe(user => expect(user).toEqual(mockUser));

    // then
    const testRequest = httpMock.expectOne(environment.apiUrl + '/api/user/me');
    expect(testRequest.request.headers.has('Authorization')).toEqual(true);
    testRequest.flush(mockUser);
  });

  afterEach(() => {
    httpMock.verify();
  })
});
