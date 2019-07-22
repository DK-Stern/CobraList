import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {Oauth2RedirectComponent} from './authentication/oauth2-redirect/oauth2-redirect.component';
import {UserModule} from './user/user.module';
import {PartyModule} from './party/authorized/party.module';
import {LogoutComponent} from './authentication/logout/logout.component';
import {LoginComponent} from './authentication/login/login.component';
import {JoinPartyComponent} from './party/join-party/join-party.component';

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'user',
    loadChildren: () => import('./user/user.module').then(mod => mod.UserModule)
  },
  {
    path: 'party',
    loadChildren: () => import('./party/authorized/party.module').then(mod => mod.PartyModule)
  },
  {
    path: 'join',
    component: JoinPartyComponent
  },
  {
    path: 'oauth2/redirect',
    component: Oauth2RedirectComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'logout',
    component: LogoutComponent
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [UserModule, PartyModule, RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
