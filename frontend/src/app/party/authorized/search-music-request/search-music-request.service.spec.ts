import {TestBed} from '@angular/core/testing';

import {SearchMusicRequestService} from './search-music-request.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('SearchMusicRequestService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule
    ]
  })
    .compileComponents());

  it('should be created', () => {
    const service: SearchMusicRequestService = TestBed.get(SearchMusicRequestService);
    expect(service).toBeTruthy();
  });
});
