import {TestBed} from '@angular/core/testing';

import {DeletePartyService} from './delete-party.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('DeletePartyService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule]
  }));

  it('should be created', () => {
    const service: DeletePartyService = TestBed.get(DeletePartyService);
    expect(service).toBeTruthy();
  });
});
