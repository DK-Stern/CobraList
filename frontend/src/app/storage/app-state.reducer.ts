import * as auth from '../authentication/store/auth.reducers';
import * as party from '../party/authorized/store/party.reducers';

export const AppStateReducer = {
  authentication: auth.reducer
};

export class AppState {
  authentication: auth.AuthState | null;
  party: party.PartyState | null;
}
