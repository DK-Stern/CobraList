import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PartyComponent} from './party/party.component';
import {PartyDetailsComponent} from './party-details/party-details.component';
import {PartyAuthGuard} from './party-auth.guard';
import {StoreModule} from '@ngrx/store';
import * as partyReducer from './store/party.reducers';
import {PartyResolverService} from './party-resolver.service';

const partyRoutes: Routes = [
  {
    path: 'party/:id',
    component: PartyComponent,
    canActivate: [PartyAuthGuard],
    resolve: [PartyResolverService],
    children: [
      {
        path: '',
        children: [
          {path: '', component: PartyDetailsComponent}
        ]
      }
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(partyRoutes),
    StoreModule.forFeature('party', partyReducer.reducer)
  ],
  exports: [RouterModule]
})
export class PartyRoutingModule {
}
