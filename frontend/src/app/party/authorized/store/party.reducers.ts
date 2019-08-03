import {Action, createReducer, on} from '@ngrx/store';
import {saveParty} from './party.actions';

export interface PartyState {
  id: string | null,
  name: string | null,
  password: string | null,
  downVoting: boolean | null,
  description: string | null
}

export const initialPartyState: PartyState = {
  id: null,
  name: null,
  password: null,
  downVoting: null,
  description: null
};

const partyReducers = createReducer(
  initialPartyState,
  on(saveParty, (state, {party}) => ({
    ...state,
    id: party.partyId,
    name: party.partyName,
    password: party.password,
    downVoting: party.downVoting,
    description: party.description
  }))
);

export function reducer(state: PartyState | undefined, action: Action) {
  return partyReducers(state, action);
}
