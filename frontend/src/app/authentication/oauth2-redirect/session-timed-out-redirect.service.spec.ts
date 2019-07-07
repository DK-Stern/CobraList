import {TestBed} from '@angular/core/testing';

import {SessionTimedOutRedirectService} from './session-timed-out-redirect.service';

describe('SessionTimedOutRedirectService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SessionTimedOutRedirectService = TestBed.get(SessionTimedOutRedirectService);
    expect(service).toBeTruthy();
  });
});
