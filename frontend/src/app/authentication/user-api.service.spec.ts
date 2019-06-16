import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {UserApiService} from './user-api.service';
import {environment} from '../../environments/environment';

describe('UserApiService', () => {

  let testSubject: UserApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });

    testSubject = TestBed.get(UserApiService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should request api to load user object', () => {
    // when
    testSubject.getUser().subscribe(user => expect(user).toBeTruthy());

    // then
    const testRequest = httpMock.expectOne(environment.apiUrl + '/api/user/me');
    expect(testRequest.request.method).toEqual('GET');
    testRequest.flush({});
  });

  afterEach(() => {
    httpMock.verify();
  });

});
