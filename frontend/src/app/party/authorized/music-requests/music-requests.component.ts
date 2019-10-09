import {Component, OnInit, ViewChild} from '@angular/core';
import {Store} from "@ngrx/store";
import {AppState} from "../../../storage/app-state.reducer";
import {MusicRequestDTO} from "../store/party-information.dto";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {FormControl} from "@angular/forms";
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import {MusicRequestVotingServiceService} from "./music-request-voting-service.service";

@Component({
  selector: 'app-music-requests',
  templateUrl: './music-requests.component.html',
  styleUrls: ['./music-requests.component.scss']
})
export class MusicRequestsComponent implements OnInit {

  displayedColumns: string[];
  columnsWithoutDownVote = [
    'position',
    'artist',
    'title',
    'duration',
    'upVotes',
    'vote'
  ];
  columnsWithDownVote = [
    'position',
    'artist',
    'title',
    'duration',
    'upVotes',
    'downVotes',
    'rating',
    'vote'
  ];

  musicRequests: MusicRequestDTO[];
  dataSource;
  filterCtrl: FormControl = new FormControl();
  partyCode: string;
  downVotable: boolean;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private store: Store<AppState>,
              private iconRegistry: MatIconRegistry,
              private sanitizer: DomSanitizer,
              public musicRequestVotingService: MusicRequestVotingServiceService) {
    iconRegistry.addSvgIcon('upVote', sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/song/thumb_up.svg'))
    iconRegistry.addSvgIcon('downVote', sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/song/thumb_down.svg'))
  }

  ngOnInit() {
    this.store.select(state => state.party).subscribe(party => {
      this.partyCode = party.partyCode;
      this.downVotable = party.downVotable;
      this.dataSource = new MatTableDataSource<MusicRequestDTO>(this.musicRequests);
      this.dataSource.sort = this.sort;
      if (this.filterCtrl.value !== "" && this.filterCtrl.value !== null) {
        this.applyFilter();
      }

      this.displayedColumns = party.downVotable ? this.columnsWithDownVote : this.columnsWithoutDownVote;

      return this.musicRequests = party.musicRequests;
    });
  }

  applyFilter() {
    if (this.filterCtrl.value !== "" && this.filterCtrl.value !== null) {
      this.dataSource.filter = this.filterCtrl.value.trim().toLowerCase();
    }
  }
}
