import {TestBed} from '@angular/core/testing';

import {JoinPartyService} from './join-party.service';
import {provideMockStore} from '@ngrx/store/testing';
import {AppState} from '../../storage/app-state.reducer';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';

describe('JoinPartyService', () => {

  const initialState: AppState = {
    authentication: null,
    party: null
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideMockStore({initialState})
      ],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule
      ]
    })
  });

  it('should be created', () => {
    const service: JoinPartyService = TestBed.get(JoinPartyService);
    expect(service).toBeTruthy();
  });
});
