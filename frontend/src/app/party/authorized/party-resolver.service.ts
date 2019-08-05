import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Store} from '@ngrx/store';
import {AppState} from '../../storage/app-state.reducer';
import {Observable, of} from 'rxjs';
import {PartyDto} from './party.dto';
import {PartyApiService} from './party-api.service';
import {mergeMap} from 'rxjs/operators';
import {saveParty} from './store/party.actions';

@Injectable({
  providedIn: 'root'
})
export class PartyResolverService implements Resolve<PartyDto> {

  constructor(private store: Store<AppState>,
              private partyApiService: PartyApiService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PartyDto> {
    let party: PartyDto = route.data.party;
    const partyUrlParamId = route.paramMap.get('id');
    if ('undefined' !== typeof party && 'undefined' !== typeof party.partyCode && partyUrlParamId == party.partyCode) {
      return of(party);
    } else {
      return this.partyApiService.getParty(partyUrlParamId).pipe(mergeMap(party => {
        this.store.dispatch(saveParty({party: party}));
        return of(party);
      }));
    }
  }
}
