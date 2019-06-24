import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {UserRoutingModule} from './user-routing.module';
import {UserDashboardComponent} from './user-dashboard/user-dashboard.component';
import {UserComponent} from './user/user.component';
import {CreatePartyComponent} from './party/create-party/create-party.component';
import {MatButtonModule, MatCardModule, MatIconModule} from '@angular/material';

@NgModule({
  declarations: [
    UserComponent,
    UserDashboardComponent,
    CreatePartyComponent
  ],
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    UserRoutingModule
  ]
})
export class UserModule {
}
