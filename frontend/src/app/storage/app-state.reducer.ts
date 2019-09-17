import * as auth from '../authentication/store/auth.reducers';
import {PartyInformationDto} from "../party/authorized/store/party-information.dto";

export const AppStateReducer = {
  authentication: auth.reducer
};

export class AppState {
  authentication: auth.AuthState | null;
  party: PartyInformationDto | null;
}
