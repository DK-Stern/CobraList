import {Component, OnInit} from '@angular/core';
import {DeletePartyService} from "./delete-party.service";
import {AppState} from "../storage/app-state.reducer";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";

@Component({
  selector: 'app-delete-party',
  templateUrl: './delete-party.component.html',
  styleUrls: ['./delete-party.component.scss']
})
export class DeletePartyComponent implements OnInit {

  constructor(private deletePartyService: DeletePartyService,
              private store: Store<AppState>,
              private router: Router) {
  }

  ngOnInit() {
    this.store.select(state => state.party.partyCode)
      .subscribe(partyCode => this.deletePartyService.deleteParty(partyCode)
        .subscribe(response => {
          this.router.navigateByUrl('user');
        }));
  }

}
