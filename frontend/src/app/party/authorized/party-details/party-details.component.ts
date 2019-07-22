import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-party-details',
  templateUrl: './party-details.component.html',
  styleUrls: ['./party-details.component.scss']
})
export class PartyDetailsComponent implements OnInit {

  party;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.data.subscribe(party => {
      this.party = party[0];
    });
  }
}
