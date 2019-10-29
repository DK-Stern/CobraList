import {Component, OnInit} from '@angular/core';
import {SessionTimedOutRedirectService} from '../oauth2-redirect/session-timed-out-redirect.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(public loginRedirectService: SessionTimedOutRedirectService) {
  }

  ngOnInit() {
  }
}
