import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {
  MatButtonModule,
  MatCardModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatProgressSpinnerModule,
  MatSidenavModule,
  MatSnackBarModule,
  MatStepperModule,
  MatToolbarModule
} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HomeComponent} from './home/home.component';
import {Oauth2RedirectComponent} from './authentication/oauth2-redirect/oauth2-redirect.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthTokenInterceptor} from './authentication/auth-token.interceptor';
import {StoreModule} from '@ngrx/store';
import * as authReducer from './authentication/store/auth.reducers';
import {EffectsModule} from '@ngrx/effects';
import {AuthEffects} from './authentication/store/auth.effect';
import {UserRoutingModule} from './user/user-routing.module';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {environment} from '../environments/environment';
import {PartyRoutingModule} from './party/authorized/party-routing.module';
import {LogoComponent} from './home/logo/logo.component';
import {LogoutComponent} from './authentication/logout/logout.component';
import {LoginComponent} from './authentication/login/login.component';
import {JoinPartyComponent} from './party/join-party/join-party.component';
import {ReactiveFormsModule} from '@angular/forms';
import {STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    Oauth2RedirectComponent,
    LogoComponent,
    LogoutComponent,
    LoginComponent,
    JoinPartyComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    UserRoutingModule,
    PartyRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatSnackBarModule,
    MatSidenavModule,
    MatListModule,
    MatProgressSpinnerModule,
    MatStepperModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    StoreModule.forRoot({authentication: authReducer.reducer}),
    StoreDevtoolsModule.instrument({
      maxAge: 25,
      logOnly: environment.production
    }),
    EffectsModule.forRoot([AuthEffects])
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthTokenInterceptor, multi: true},
    {provide: STEPPER_GLOBAL_OPTIONS, useValue: {showError: true}}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
