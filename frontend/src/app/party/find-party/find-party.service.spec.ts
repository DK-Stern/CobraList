import {TestBed} from '@angular/core/testing';

import {FindPartyService} from './find-party.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('FindPartyService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    })
  });

  it('should be created', () => {
    const service: FindPartyService = TestBed.get(FindPartyService);
    expect(service).toBeTruthy();
  });
});
