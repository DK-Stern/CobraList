import {TestBed} from '@angular/core/testing';

import {TokenStorageService} from './token-storage.service';
import {MockStore, provideMockStore} from '@ngrx/store/testing';
import {UserValueObject} from './UserValueObject';

describe('TokenStorageService', () => {
  let testSubject: TokenStorageService;
  let store: MockStore<{ user: UserValueObject }>;
  const initialState = {user: null};

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideMockStore({initialState})
      ]
    });

    testSubject = new TokenStorageService();
  });

  it('should be created', () => {
    const service: TokenStorageService = TestBed.get(TokenStorageService);
    expect(service).toBeTruthy();
  });
});
