import {Action, createReducer, on} from '@ngrx/store';
import {UserValueObject} from '../../user/user.value.object';
import {loadedUser, loadedUserFail, loginSuccess, logout} from './auth.actions';

export interface AuthState {
  isAuthenticated: boolean,
  token: string | null,
  user: UserValueObject | null,
  error: Error | null
}

export const initialAuthenticationState: AuthState = {
  isAuthenticated: false,
  token: null,
  user: null,
  error: null
};

const authReducer = createReducer(
  initialAuthenticationState,
  on(loadedUser, (state, {user}) => ({...state, user: user, error: null})),
  on(loadedUserFail, (state, {error}) => ({...state, isAuthenticated: false, token: null, user: null, error: error})),
  on(loginSuccess, (state, {token}) => ({...state, isAuthenticated: true, token: token})),
  on(logout, (state => ({...state, isAuthenticated: false, token: null, user: null, error: null}))));

export function reducer(state: AuthState | undefined, action: Action) {
  return authReducer(state, action);
}
