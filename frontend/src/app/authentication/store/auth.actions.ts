import {createAction, props} from '@ngrx/store';
import {UserDto} from '../../user/user.dto';

export const loginSuccess = createAction(
  '[OAuth2 Redirect Component | App Component] Login Success',
  props<{ token: string }>()
);

export const loginGuestSuccess = createAction(
  '[Join Party] Login Guest Success',
  props<{ token: string }>()
);

export const loadedUser = createAction(
  '[User API] User Loaded Success',
  props<{ user: UserDto }>()
);

export const loadedUserFail = createAction(
  '[User API] User Loaded Error',
  props<{ error: Error }>()
);

export const logout = createAction(
  '[App Component] Logout User'
);
