import {Component, OnInit, ViewChild} from '@angular/core';
import {Store} from "@ngrx/store";
import {AppState} from "../../../storage/app-state.reducer";
import {MusicRequestDTO} from "../store/party-information.dto";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-music-requests',
  templateUrl: './music-requests.component.html',
  styleUrls: ['./music-requests.component.scss']
})
export class MusicRequestsComponent implements OnInit {

  displayedColumns: string[] = [
    'artist',
    'title',
    'duration',
    'upVotes',
    'downVotes',
    'rating'];

  musicRequests: MusicRequestDTO[];
  dataSource;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private store: Store<AppState>) {
  }

  ngOnInit() {
    this.store.select(state => state.party.musicRequests).subscribe(musicRequests => this.musicRequests = musicRequests);
    this.dataSource = new MatTableDataSource<MusicRequestDTO>(this.musicRequests);
    this.dataSource.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
