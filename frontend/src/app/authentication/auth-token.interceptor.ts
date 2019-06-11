import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';
import {AppState} from '../storage/appStateReducer';

const TOKEN_HEADER_KEY = "Authorization";

@Injectable()
export class AuthTokenInterceptor implements HttpInterceptor {

  token$ = this.store.select(state => state.authState.token);
  token: string;

  constructor(private store: Store<AppState>) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    this.token$.subscribe(token => this.token = token);
    if (this.token != null) {
      authReq = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + this.token)});
    }

    return next.handle(authReq);
  }
}
