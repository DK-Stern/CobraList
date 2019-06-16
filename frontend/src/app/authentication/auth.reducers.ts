import {Action, createReducer, on} from '@ngrx/store';
import {UserValueObject} from './user.value.object';
import {loadedUser, loggedIn} from './auth.actions';

export interface State {
  isAuthenticated: boolean,
  token: string | null,
  user: UserValueObject | null
}

const initialAuthState: State = {
  isAuthenticated: false,
  token: null,
  user: null
};

const authReducer = createReducer(
  initialAuthState,
  on(loadedUser, (state, {user}) => ({...state, user: user})),
  on(loggedIn, (state, {token}) => ({...state, isAuthenticated: true, token: token})));

export function reducer(state: State | undefined, action: Action) {
  return authReducer(state, action);
}
