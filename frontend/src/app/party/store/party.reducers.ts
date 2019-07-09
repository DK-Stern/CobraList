import {Action, createReducer, on} from '@ngrx/store';
import {saveParty} from './party.actions';

export interface PartyState {
  id: string | null,
  name: string | null,
  password: string | null,
  downVoting: boolean | null,
}

const initialPartyState: PartyState = {
  id: null,
  name: null,
  password: null,
  downVoting: null
};

const partyReducers = createReducer(
  initialPartyState,
  on(saveParty, (state, {party}) => ({
    ...state,
    id: party.id,
    name: party.name,
    password: party.password,
    downVoting: party.downVoting
  }))
);

export function reducer(state: PartyState | undefined, action: Action) {
  return partyReducers(state, action);
}