import {Injectable} from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild,
  CanLoad,
  Route,
  Router,
  RouterStateSnapshot,
  UrlSegment,
  UrlTree
} from '@angular/router';
import {Observable} from 'rxjs';
import {AppState} from '../storage/app-state.reducer';
import {Store} from '@ngrx/store';

@Injectable({
  providedIn: 'root'
})
export class UserAuthGuard implements CanActivate, CanActivateChild, CanLoad {

  isLoggedIn: boolean = false;

  constructor(private storage: Store<AppState>,
              private router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    this.checkUserIsLoggedIn();
    return this.isLoggedIn;
  }


  canActivateChild(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    this.checkUserIsLoggedIn();
    return this.isLoggedIn;
  }

  canLoad(
    route: Route,
    segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {

    this.checkUserIsLoggedIn();
    return this.isLoggedIn;
  }

  private checkUserIsLoggedIn() {
    if (!this.isLoggedIn) {
      this.storage.select(state => state.authentication).subscribe(authentication => {
        this.isLoggedIn = authentication.isAuthenticated && !authentication.isGuest;
        if (!this.isLoggedIn && this.router.url !== '/logout') {
          this.router.navigateByUrl('login');
        }
      });
    }
  }
}
