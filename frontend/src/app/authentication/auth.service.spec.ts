import {TestBed} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {environment} from '../../environments/environment';
import {UserValueObject} from './UserValueObject';

describe('AuthService', () => {

  let testSubject: AuthService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });

    testSubject = TestBed.get(AuthService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    const service: AuthService = TestBed.get(AuthService);
    expect(service).toBeTruthy();
  });

  it('function get users should use http client', () => {
    // given
    const mockUser: UserValueObject = {
      id: 1,
      name: 'Max',
      email: 'max@email.de',
      authorities: ['USER']
    };

    // when
    let resultedObservable = testSubject.getUser();

    // then
    resultedObservable.subscribe(user => expect(user).toEqual(mockUser));

    let testRequest = httpTestingController.expectOne(environment.apiUrl + '/api/user/me');
    expect(testRequest.request.method).toEqual('GET');
    testRequest.flush(mockUser);
  });

  afterEach(() => {
    httpTestingController.verify();
  })
});
