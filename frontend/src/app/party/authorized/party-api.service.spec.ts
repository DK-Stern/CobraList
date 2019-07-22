import {TestBed} from '@angular/core/testing';

import {PartyApiService} from './party-api.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('PartyApiService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule]
  }));

  it('should be created', () => {
    const service: PartyApiService = TestBed.get(PartyApiService);
    expect(service).toBeTruthy();
  });
});
