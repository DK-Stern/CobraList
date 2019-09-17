import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PartyRoutingModule} from './party-routing.module';
import {PartyDetailsComponent} from './party-details/party-details.component';
import {PartyComponent} from './party/party.component';
import {EffectsModule} from '@ngrx/effects';
import {PartyEffect} from './store/party.effect';
import {PartyInformationComponent} from './party-information/party-information.component';
import {MusicRequestsComponent} from './music-requests/music-requests.component';
import {PlayerComponent} from './player/player.component';
import {MatTableModule} from "@angular/material/table";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSortModule} from "@angular/material/sort";

@NgModule({
  declarations: [PartyDetailsComponent, PartyComponent, PartyInformationComponent, MusicRequestsComponent, PlayerComponent],
  imports: [
    CommonModule,
    PartyRoutingModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSortModule,
    EffectsModule.forFeature([PartyEffect])
  ]
})
export class PartyModule {
}
