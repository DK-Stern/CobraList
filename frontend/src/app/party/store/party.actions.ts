import {createAction, props} from '@ngrx/store';
import {PartyCreationValueObject} from '../../user/party/create-party/party-creation-value.object';

export const saveParty = createAction(
  '[Party Creation Component] Save Party',
  props<{ party: PartyCreationValueObject }>()
);

export const loadParty = createAction(
  '[Party Resolver] Load Party from API',
  props<{ id: string; }>()
);

export const errorLoadingParty = createAction(
  '[Party API Service] Error loading party',
  props<{ error: string }>()
);
