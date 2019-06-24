import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserComponent} from './user/user.component';
import {CreatePartyComponent} from './party/create-party/create-party.component';
import {UserDashboardComponent} from './user-dashboard/user-dashboard.component';
import {AuthGuard} from '../authentication/auth.guard';

const userRoutes: Routes = [
  {
    path: 'user',
    component: UserComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        children: [
          {path: 'party/create', component: CreatePartyComponent},
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
