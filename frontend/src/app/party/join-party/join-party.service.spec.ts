import {TestBed} from '@angular/core/testing';

import {JoinPartyService} from './join-party.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('JoinPartyService', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule]
    })
  });

  it('should be created', () => {
    const service: JoinPartyService = TestBed.get(JoinPartyService);
    expect(service).toBeTruthy();
  });
});
