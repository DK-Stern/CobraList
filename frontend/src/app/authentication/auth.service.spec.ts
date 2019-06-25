import {TestBed} from '@angular/core/testing';

import {UserApiService} from '../user/user-api.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {environment} from '../../environments/environment';
import {UserValueObject} from '../user/user.value.object';

describe('UserApiService', () => {

  let testSubject: UserApiService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });

    testSubject = TestBed.get(UserApiService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(testSubject).toBeTruthy();
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
