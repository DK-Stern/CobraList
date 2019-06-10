import {Component} from '@angular/core';
import {environment} from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'CobraList';
  spotify_auth_url = environment.apiUrl + '/oauth2/authorize/spotify?redirect_uri=' + environment.oauthRedirect;
}
