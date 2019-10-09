import {Action, createReducer, on} from '@ngrx/store';
import {saveParty} from './party.actions';
import {PartyInformationDto} from "./party-information.dto";


export const initialPartyState: PartyInformationDto = {
  partyCode: null,
  downVotable: null,
  currentPlayback: null,
  musicRequests: null
};

const partyReducers = createReducer(
  initialPartyState,
  on(saveParty, (state, {party}) => ({
    ...state,
    partyCode: party.partyCode,
    downVotable: party.downVotable,
    currentPlayback: party.currentPlayback,
    musicRequests: party.musicRequests
  }))
);

export function reducer(state: PartyInformationDto | undefined, action: Action) {
  return partyReducers(state, action);
}
