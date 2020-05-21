import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../../storage/app-state.reducer';
import {CurrentPlaybackDTO, CurrentTrackDTO} from '../store/party-information.dto';
import {MatIconRegistry} from '@angular/material/icon';
import {DomSanitizer} from '@angular/platform-browser';
import {PlayerService} from './player.service';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  isCreator: boolean;
  partyCode: string;
  currentPlayback: CurrentPlaybackDTO;
  currentTrack: CurrentTrackDTO;

  constructor(private store: Store<AppState>,
              private iconRegistry: MatIconRegistry,
              private sanitizer: DomSanitizer,
              public playerService: PlayerService) {
    iconRegistry.addSvgIcon('play', sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/player/play.svg'));
    iconRegistry.addSvgIcon('pause', sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/player/pause.svg'));
    iconRegistry.addSvgIcon('skip', sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/player/skip_next.svg'));
  }

  ngOnInit() {
    this.store.select(state => state).subscribe(state => {
      this.isCreator = !state.authentication.isGuest;
      this.partyCode = state.party.partyCode;
      this.currentPlayback = state.party.currentPlayback;
      if (this.currentPlayback !== null) {
        this.currentTrack = this.currentPlayback.currentTrack;
      }
    });
  }
}
