import {createAction, props} from '@ngrx/store';
import {PartyDto} from '../party.dto';

export const saveParty = createAction(
  '[Party Creation Component] Save Party',
  props<{ party: PartyDto }>()
);

export const loadParty = createAction(
  '[Party Resolver] Load Party from API',
  props<{ id: string; }>()
);

export const errorLoadingParty = createAction(
  '[Party API Service] Error loading party',
  props<{ error: string }>()
);
