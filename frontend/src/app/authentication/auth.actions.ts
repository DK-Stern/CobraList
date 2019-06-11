import {createAction, props} from '@ngrx/store';
import {UserValueObject} from './UserValueObject';

export const loggedIn = createAction(
  '[OAuth2 Redirect Page] Login Success',
  props<{ token: string }>()
);

export const loadedUser = createAction(
  '[User API] User Loaded Success',
  props<{ user: UserValueObject }>()
);
