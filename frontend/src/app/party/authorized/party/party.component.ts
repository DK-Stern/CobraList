import {Component, OnDestroy, OnInit} from '@angular/core';
import {interval, Subscription} from "rxjs";
import {AppState} from "../../../storage/app-state.reducer";
import {Store} from "@ngrx/store";
import {loadParty} from "../store/party.actions";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-party',
  templateUrl: './party.component.html',
  styleUrls: ['./party.component.scss']
})
export class PartyComponent implements OnInit, OnDestroy {

  private subscription: Subscription;

  constructor(private route: ActivatedRoute,
              private store: Store<AppState>) {
  }

  ngOnInit() {
    const source = interval(environment.partyRefreshTime);
    this.subscription = source.subscribe(() =>
      this.route.paramMap.subscribe((params: ParamMap) =>
        this.store.dispatch(loadParty({partyCode: params.get('id')}))));
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
