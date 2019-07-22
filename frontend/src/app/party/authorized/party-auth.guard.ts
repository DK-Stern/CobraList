import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from '@angular/router';
import {Store} from '@ngrx/store';
import {AppState} from '../../storage/app-state.reducer';

@Injectable({
  providedIn: 'root'
})
export class PartyAuthGuard implements CanActivate {

  isLoggedIn: boolean;

  constructor(private storage: Store<AppState>) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    this.checkLoginUser();
    return this.isLoggedIn;
  }

  private checkLoginUser() {
    if (!this.isLoggedIn) {
      this.storage.select(state => state.authentication.isAuthenticated).subscribe(isAuthenticated => {
        if (isAuthenticated) {
          this.isLoggedIn = isAuthenticated;
        }
      });
    }
  }
}
