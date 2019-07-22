import {TestBed} from '@angular/core/testing';

import {PartyEffect} from './party.effect';
import {Observable} from 'rxjs';
import {provideMockActions} from '@ngrx/effects/testing';
import {PartyApiService} from './party-api.service';

describe('PartyEffect', () => {
  let actions: Observable<any>;

  beforeEach(() => {
    let partyApiServiceSpy = jasmine.createSpyObj('PartyApiService', ['getParty']);

    TestBed.configureTestingModule({
      providers: [
        PartyEffect,
        {
          provide: PartyApiService,
          useValue: partyApiServiceSpy
        },
        provideMockActions(() => actions)
      ]
    })
  });

  it('should be created', () => {
    const service: PartyEffect = TestBed.get(PartyEffect);
    expect(service).toBeTruthy();
  });
});
