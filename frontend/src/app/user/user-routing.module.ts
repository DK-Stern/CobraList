import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserComponent} from './user/user.component';
import {CreatePartyComponent} from './party/create-party/create-party.component';
import {UserDashboardComponent} from './user-dashboard/user-dashboard.component';
import {AuthGuard} from '../authentication/auth.guard';
import {BasePlaylistResolverService} from './party/create-party/base-playlist/base-playlist-resolver.service';

const userRoutes: Routes = [
  {
    path: 'user',
    component: UserComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        children: [
          {
            path: 'party/create',
            component: CreatePartyComponent,
            resolve: {
              basePlaylists: BasePlaylistResolverService
            }
          },
          {path: '', component: UserDashboardComponent}
        ]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(userRoutes)],
  exports: [RouterModule]
})
export class UserRoutingModule {
}
