import {TestBed} from '@angular/core/testing';

import {PartyResolverService} from './party-resolver.service';
import {PartyApiService} from './party-api.service';
import {provideMockStore} from '@ngrx/store/testing';

describe('PartyResolverService', () => {
  beforeEach(() => {
    let partyApiServiceSpy = jasmine.createSpyObj('PartyApiService', ['getParty']);
    let initialState = {
      authentication: null,
      party: null
    };

    TestBed.configureTestingModule({
      providers: [
        {
          provide: PartyApiService,
          useValue: partyApiServiceSpy
        },
        provideMockStore({initialState})
      ]
    })
  });

  it('should be created', () => {
    const service: PartyResolverService = TestBed.get(PartyResolverService);
    expect(service).toBeTruthy();
  });
});
