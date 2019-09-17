import {Action, createReducer, on} from '@ngrx/store';
import {saveParty} from './party.actions';
import {PartyInformationDto} from "./party-information.dto";


export const initialPartyState: PartyInformationDto = {
  currentPlayback: null,
  musicRequests: null
};

const partyReducers = createReducer(
  initialPartyState,
  on(saveParty, (state, {party}) => ({
    ...state,
    currentPlayback: party.currentPlayback,
    musicRequests: party.musicRequests
  }))
);

export function reducer(state: PartyInformationDto | undefined, action: Action) {
  return partyReducers(state, action);
}
