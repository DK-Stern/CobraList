import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Store} from '@ngrx/store';
import {AppState} from '../storage/app-state.reducer';
import {Observable, of} from 'rxjs';
import {PartyCreationValueObject} from '../user/party/create-party/party-creation-value.object';
import {PartyApiService} from './party-api.service';
import {mergeMap} from 'rxjs/operators';
import {saveParty} from './store/party.actions';
import {PartyValueObject} from './party.value-object';

@Injectable({
  providedIn: 'root'
})
export class PartyResolverService implements Resolve<PartyValueObject> {

  constructor(private store: Store<AppState>,
              private partyApiService: PartyApiService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PartyValueObject> {
    let party: PartyCreationValueObject = route.data.party;
    const partyUrlParamId = route.paramMap.get('id');
    if ('undefined' !== typeof party && 'undefined' !== typeof party.id && partyUrlParamId == party.id) {
      return of(party);
    } else {
      return this.partyApiService.getParty(partyUrlParamId).pipe(mergeMap(party => {
        this.store.dispatch(saveParty({party: party}));
        return of(party);
      }));
    }
  }
}
