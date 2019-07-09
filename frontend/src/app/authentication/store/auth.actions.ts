import {createAction, props} from '@ngrx/store';
import {UserValueObject} from '../../user/user.value.object';

export const loggedIn = createAction(
  '[OAuth2 Redirect Page] Login Success',
  props<{ token: string }>()
);

export const loadedUser = createAction(
  '[User API] User Loaded Success',
  props<{ user: UserValueObject }>()
);

export const loadedUserFail = createAction(
  '[User API] User Loaded Error',
  props<{ error: Error }>()
);
