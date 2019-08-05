import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {JoinPartyDto} from './join-party.dto';
import {environment} from '../../../environments/environment';
import {Router} from '@angular/router';
import {AppState} from '../../storage/app-state.reducer';
import {Store} from '@ngrx/store';
import {loginGuestSuccess} from '../../authentication/store/auth.actions';

interface PartyJoinedDto {
  token: string,
  partyCode: string
}

@Injectable({
  providedIn: 'root'
})
export class JoinPartyService {

  constructor(private httpClient: HttpClient,
              private store: Store<AppState>,
              private router: Router) {
  }


  joinParty(joinPartyDto: JoinPartyDto) {
    this.httpClient.post<PartyJoinedDto>(environment.apiUrl + '/api/party/join/', joinPartyDto)
      .subscribe(response => {
        this.store.dispatch(loginGuestSuccess({token: response.token}));
        this.router.navigate(['/party', response.partyCode]);
      });
  }
}
