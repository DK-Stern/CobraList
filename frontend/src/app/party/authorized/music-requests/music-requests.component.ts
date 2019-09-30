import {Component, OnInit, ViewChild} from '@angular/core';
import {Store} from "@ngrx/store";
import {AppState} from "../../../storage/app-state.reducer";
import {MusicRequestDTO} from "../store/party-information.dto";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-music-requests',
  templateUrl: './music-requests.component.html',
  styleUrls: ['./music-requests.component.scss']
})
export class MusicRequestsComponent implements OnInit {

  displayedColumns: string[] = [
    'position',
    'artist',
    'title',
    'duration',
    'upVotes',
    'downVotes',
    'rating'];

  musicRequests: MusicRequestDTO[];
  dataSource;
  filterCtrl: FormControl = new FormControl();

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private store: Store<AppState>) {
  }

  ngOnInit() {
    this.store.select(state => state.party.musicRequests).subscribe(musicRequests => {
      this.dataSource = new MatTableDataSource<MusicRequestDTO>(this.musicRequests);
      this.dataSource.sort = this.sort;
      if (this.filterCtrl.value !== "" && this.filterCtrl.value !== null) {
        this.applyFilter();
      }
      return this.musicRequests = musicRequests;
    });
  }

  applyFilter() {
    if (this.filterCtrl.value !== "" && this.filterCtrl.value !== null) {
      this.dataSource.filter = this.filterCtrl.value.trim().toLowerCase();
    }
  }
}
