import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {TokenStorageService} from '../token-storage.service';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-oauth2-redirect',
  templateUrl: './oauth2-redirect.component.html',
  styleUrls: ['./oauth2-redirect.component.scss']
})
export class Oauth2RedirectComponent implements OnInit {
  token: string;
  error: string;

  constructor(private route: ActivatedRoute,
              private tokenService: TokenStorageService,
              private httpClient: HttpClient) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.token = params["token"];
      this.error = params["error"];

      if (this.token != null) {
        this.tokenService.saveToken(this.token);

        this.httpClient.get(environment.apiUrl + "/api/user/me")
          .subscribe(user => console.log(user));
      }
    })
  }
}
