import {TestBed} from '@angular/core/testing';

import {BasePlaylistService} from './base-playlist.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('BasePlaylistService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    })
      .compileComponents();
  });

  it('should be created', () => {
    const service: BasePlaylistService = TestBed.get(BasePlaylistService);
    expect(service).toBeTruthy();
  });
});
