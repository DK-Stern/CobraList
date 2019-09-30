import {Component, OnInit} from '@angular/core';
import {SearchMusicRequestDto} from "./search.music-request.dto";
import {FormControl} from "@angular/forms";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {debounceTime, finalize, switchMap, tap} from "rxjs/operators";
import {SearchMusicRequestService} from "./search-music-request.service";
import {NEVER, Observable, of} from "rxjs";
import {AddMusicRequestDto} from "./add.music-request.dto";

@Component({
  selector: 'app-search-music-request',
  templateUrl: './search-music-request.component.html',
  styleUrls: ['./search-music-request.component.scss']
})
export class SearchMusicRequestComponent implements OnInit {
  searchMusicRequestCtrl: FormControl = new FormControl();
  foundMusicRequests: SearchMusicRequestDto[] | Observable<never>;
  isLoading: boolean = false;
  errorMessage: string;
  partyCode: string;

  constructor(private route: ActivatedRoute,
              private searchMusicRequestService: SearchMusicRequestService) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.partyCode = params.get("id");
      this.searchMusicRequestCtrl.valueChanges
        .pipe(debounceTime(500),
          tap(() => {
            this.foundMusicRequests = [];
            this.errorMessage = "";
            this.isLoading = true;
          }),
          switchMap(value => {
              return value !== "" && 'undefined' !== typeof value
                ? this.searchMusicRequestService.searchMusicRequest(this.partyCode, value)
                  .pipe(
                    finalize(() => this.isLoading = false))
                : of(NEVER);
            }
          )
        )
        .subscribe(data => {
          if (data['message'] == undefined && data !== NEVER) {
            this.foundMusicRequests = data;
          } else {
            this.errorMessage = data['message'];
          }
        });
    });
  }

  addMusicRequest(musicRequest: SearchMusicRequestDto) {
    if (!musicRequest.alreadyInPlaylist) {
      const addMusicRequestDTO: AddMusicRequestDto = {
        partyCode: this.partyCode,
        trackDTO: {
          id: musicRequest.trackId,
          albumName: musicRequest.albumName,
          artists: musicRequest.artists,
          duration: musicRequest.duration,
          imageHeight: musicRequest.imageHeight,
          imageWidth: musicRequest.imageWidth,
          imageUrl: musicRequest.imageUrl,
          name: musicRequest.name,
          uri: musicRequest.uri
        }
      };
      this.searchMusicRequestService.addMusicRequest(addMusicRequestDTO).subscribe(data => console.log("Musikwunsch hinzugefügt."));
    }
  }

}
