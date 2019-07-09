import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PartyRoutingModule} from './party-routing.module';
import {PartyDetailsComponent} from './party-details/party-details.component';
import {PartyComponent} from './party/party.component';
import {EffectsModule} from '@ngrx/effects';
import {PartyEffect} from './party.effect';

@NgModule({
  declarations: [PartyDetailsComponent, PartyComponent],
  imports: [
    CommonModule,
    PartyRoutingModule,
    EffectsModule.forFeature([PartyEffect])
  ]
})
export class PartyModule {
}
