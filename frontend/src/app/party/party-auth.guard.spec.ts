import {TestBed} from '@angular/core/testing';

import {PartyAuthGuard} from './party-auth.guard';
import {provideMockStore} from '@ngrx/store/testing';
import {AppState} from '../storage/app-state.reducer';

describe('PartyAuthGuard', () => {
  beforeEach(() => {
    let initialState: AppState = {
      authentication: null,
      party: null
    };

    TestBed.configureTestingModule({
      providers: [
        provideMockStore({initialState})
      ]
    })
  });

  it('should be created', () => {
    const service: PartyAuthGuard = TestBed.get(PartyAuthGuard);
    expect(service).toBeTruthy();
  });
});
