import {Action, createReducer, on} from '@ngrx/store';
import {UserValueObject} from '../../user/user.value.object';
import {loadedUser, loadedUserFail, loggedIn} from './auth.actions';

export interface AuthState {
  isAuthenticated: boolean,
  token: string | null,
  user: UserValueObject | null,
  error: Error | null
}

const initialauthentication: AuthState = {
  isAuthenticated: false,
  token: null,
  user: null,
  error: null
};

const authReducer = createReducer(
  initialauthentication,
  on(loadedUser, (state, {user}) => ({...state, user: user, error: null})),
  on(loadedUserFail, (state, {error}) => ({...state, isAuthenticated: false, token: null, user: null, error: error})),
  on(loggedIn, (state, {token}) => ({...state, isAuthenticated: true, token: token})));

export function reducer(state: AuthState | undefined, action: Action) {
  return authReducer(state, action);
}
