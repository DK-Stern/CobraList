import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Store} from '@ngrx/store';
import {AppState} from '../../storage/app-state.reducer';
import {Observable, of} from 'rxjs';
import {PartyApiService} from './party-api.service';
import {mergeMap} from 'rxjs/operators';
import {saveParty} from './store/party.actions';
import {PartyInformationDto} from "./store/party-information.dto";

@Injectable({
  providedIn: 'root'
})
export class PartyResolverService implements Resolve<PartyInformationDto> {

  constructor(private store: Store<AppState>,
              private partyApiService: PartyApiService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PartyInformationDto> {
    const partyUrlParamId = route.paramMap.get('id');
    return this.partyApiService.getPartyInformation(partyUrlParamId).pipe(mergeMap(party => {
        this.store.dispatch(saveParty({party: party}));
        return of(party);
      }));
  }
}
