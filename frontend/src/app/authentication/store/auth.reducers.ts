import {Action, createReducer, on} from '@ngrx/store';
import {UserDto} from '../../user/user.dto';
import {loadedUser, loadedUserFail, loginGuestSuccess, loginSuccess, logout} from './auth.actions';

export interface AuthState {
  isAuthenticated: boolean,
  isGuest: boolean,
  token: string | null,
  user: UserDto | null,
  error: Error | null
}

export const initialAuthenticationState: AuthState = {
  isAuthenticated: false,
  isGuest: false,
  token: null,
  user: null,
  error: null
};

const authReducer = createReducer(
  initialAuthenticationState,
  on(loadedUser, (state, {user}) => ({...state, user: user, error: null})),
  on(loadedUserFail, (state, {error}) => ({...state, isAuthenticated: false, token: null, user: null, error: error})),
  on(loginSuccess, (state, {token}) => ({...state, isAuthenticated: true, isGuest: false, token: token})),
  on(loginGuestSuccess, (state, {token, guest}) => ({
    ...state,
    isAuthenticated: true,
    isGuest: true,
    token: token,
    user: guest
  })),
  on(logout, (state => ({...state, isAuthenticated: false, token: null, user: null, error: null}))));

export function reducer(state: AuthState | undefined, action: Action) {
  return authReducer(state, action);
}
