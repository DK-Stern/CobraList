import {createAction, props} from '@ngrx/store';
import {PartyInformationDto} from "./party-information.dto";

export const saveParty = createAction(
  '[Party Creation Component] Save Party',
  props<{ party: PartyInformationDto }>()
);

export const loadParty = createAction(
  '[Party Resolver] Load Party from API',
  props<{ partyCode: string; }>()
);

export const errorLoadingParty = createAction(
  '[Party API Service] Error loading party',
  props<{ error: string }>()
);
