import {TestBed} from '@angular/core/testing';

import {BasePlaylistResolverService} from './base-playlist-resolver.service';
import {BasePlaylistService} from './base-playlist.service';
import {MatSnackBarModule} from '@angular/material';
import createSpyObj = jasmine.createSpyObj;

describe('BasePlaylistResolverService', () => {

  beforeEach(() => {
    const basePlaylistServiceStub = createSpyObj('BasePlaylistService', ['getBasePlaylists']);

    TestBed.configureTestingModule({
      providers: [
        {
          provide: BasePlaylistService,
          useValu: basePlaylistServiceStub
        }
      ],
      imports: [
        MatSnackBarModule
      ]
    })
  });

  it('should be created', () => {
    const service: BasePlaylistResolverService = TestBed.get(BasePlaylistResolverService);
    expect(service).toBeTruthy();
  });
});
