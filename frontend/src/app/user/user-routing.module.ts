import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserComponent} from './user/user.component';
import {CreatePartyComponent} from './create-party/create-party.component';
import {UserDashboardComponent} from './user-dashboard/user-dashboard.component';
import {UserAuthGuard} from '../authentication/user-auth.guard';
import {BasePlaylistResolverService} from './create-party/base-playlist/base-playlist-resolver.service';

const userRoutes: Routes = [
  {
    path: 'user',
    component: UserComponent,
    canActivate: [UserAuthGuard],
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
