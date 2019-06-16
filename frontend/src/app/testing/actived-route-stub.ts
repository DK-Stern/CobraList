import {ReplaySubject} from 'rxjs';
import {Params} from '@angular/router';

export class ActivedRouteStub {
  private subject = new ReplaySubject();

  constructor(initialParams?: Params) {
    this.setQueryParams(initialParams);
  }

  readonly queryParams = this.subject.asObservable();

  setQueryParams(params?: Params) {
    this.subject.next(params);
  }
}
