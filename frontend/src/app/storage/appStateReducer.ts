import * as auth from '../authentication/auth.reducers';

export const AppStateReducer = {
  authState: auth.reducer
};

export class AppState {
  authState: auth.AuthState
}
