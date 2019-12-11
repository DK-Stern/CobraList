import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../storage/app-state.reducer';
import {logout} from '../store/auth.actions';
import {Router} from '@angular/router';
import {LocalStorageService, STORAGE_KEY} from '../../storage/local-storage.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {

  constructor(private store: Store<AppState>,
              private router: Router,
              private localStorageService: LocalStorageService,) {
  }

  ngOnInit() {
    this.localStorageService.clearAll();
    this.store.dispatch(logout());

    setTimeout(() => this.router.navigateByUrl('home'), 2000);
  }
}
