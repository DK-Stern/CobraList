import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {AppState} from "../../../storage/app-state.reducer";
import {CurrentPlaybackDTO, CurrentTrackDTO} from "../store/party-information.dto";

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  currentPlayback: CurrentPlaybackDTO;
  currentTrack: CurrentTrackDTO;

  constructor(private store: Store<AppState>) {
  }

  ngOnInit() {
    this.store.select(state => state.party.currentPlayback).subscribe(currentPlayback => {
      this.currentPlayback = currentPlayback;
      if (currentPlayback !== null) {
        this.currentTrack = currentPlayback.currentTrack;
      }
    });
  }
}
