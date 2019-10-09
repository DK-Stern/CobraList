import {TestBed} from '@angular/core/testing';

import {MusicRequestVotingServiceService} from './music-request-voting-service.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('MusicRequestVotingServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule
    ]
  }));

  it('should be created', () => {
    const service: MusicRequestVotingServiceService = TestBed.get(MusicRequestVotingServiceService);
    expect(service).toBeTruthy();
  });
});
