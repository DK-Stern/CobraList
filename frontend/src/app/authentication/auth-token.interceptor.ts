import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {Store} from '@ngrx/store';
import {AppState} from '../storage/app-state.reducer';
import {MatSnackBar} from '@angular/material';
import {catchError} from 'rxjs/operators';
import {SessionTimedOutRedirectService} from './oauth2-redirect/session-timed-out-redirect.service';

const TOKEN_HEADER_KEY = "Authorization";

@Injectable()
export class AuthTokenInterceptor implements HttpInterceptor {

  token$ = this.store.select(state => state.authentication.token);
  token: string;

  constructor(private store: Store<AppState>,
              private _snackBar: MatSnackBar,
              private sessionTimedOutService: SessionTimedOutRedirectService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    this.token$.subscribe(token => this.token = token);
    if (this.token != null) {
      authReq = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + this.token)});
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        this._snackBar.open('Ups, da ist wohl was schief gelaufen. :(', 'Neu einloggen', {
          duration: 0,
        }).onAction().subscribe(() => this.sessionTimedOutService.redirectLogin());
        return throwError(error);
      })
    );
  }
}
