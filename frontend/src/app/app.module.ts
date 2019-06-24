import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {
  MatButtonModule,
  MatCardModule,
  MatIconModule,
  MatMenuModule,
  MatProgressSpinnerModule,
  MatToolbarModule
} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HomeComponent} from './home/home.component';
import {Oauth2RedirectComponent} from './authentication/oauth2-redirect/oauth2-redirect.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthTokenInterceptor} from './authentication/auth-token.interceptor';
import {StoreModule} from '@ngrx/store';
import {AppStateReducer} from './storage/appStateReducer';
import {EffectsModule} from '@ngrx/effects';
import {AuthEffects} from './authentication/auth.effect';
import {UserRoutingModule} from './user/user-routing.module';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    Oauth2RedirectComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    UserRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatProgressSpinnerModule,
    MatIconModule,
    StoreModule.forRoot(AppStateReducer),
    EffectsModule.forRoot([AuthEffects])
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthTokenInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
