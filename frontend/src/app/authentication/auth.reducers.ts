import {Action, createReducer, on} from '@ngrx/store';
import {UserValueObject} from './user.value.object';
import {loadedUser, loadedUserFail, loggedIn} from './auth.actions';

export interface State {
  isAuthenticated: boolean,
  token: string | null,
  user: UserValueObject | null,
  error: Error | null
}

const initialAuthState: State = {
  isAuthenticated: false,
  token: null,
  user: null,
  error: null
};

const authReducer = createReducer(
  initialAuthState,
  on(loadedUser, (state, {user}) => ({...state, user: user, error: null})),
  on(loadedUserFail, (state, {error}) => ({...state, error: error})),
  on(loggedIn, (state, {token}) => ({...state, isAuthenticated: true, token: token})));

export function reducer(state: State | undefined, action: Action) {
  return authReducer(state, action);
}
