import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {PartyApiService} from './party-api.service';
import {errorLoadingParty, loadParty, saveParty} from './store/party.actions';
import {catchError, exhaustMap, map} from 'rxjs/operators';
import {of} from 'rxjs';

@Injectable()
export class PartyEffect {

  constructor(private actions$: Actions,
              private partyApiService: PartyApiService) {
  }

  loadParty$ = createEffect(() => this.actions$.pipe(
    ofType(loadParty),
    exhaustMap(action => this.partyApiService.getParty(action.id).pipe(
      map(party => saveParty({party: party})),
      catchError(error => of(errorLoadingParty({error: error.toString()})))
      )
    )
  ));
}
