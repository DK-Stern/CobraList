import {TestBed} from '@angular/core/testing';

import {MusicRequestVotingService} from './music-request-voting.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('MusicRequestVotingService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule
    ]
  }));

  it('should be created', () => {
    const service: MusicRequestVotingService = TestBed.get(MusicRequestVotingService);
    expect(service).toBeTruthy();
  });
});
