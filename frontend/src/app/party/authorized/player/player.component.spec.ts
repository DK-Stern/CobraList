import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PlayerComponent} from './player.component';
import {provideMockStore} from "@ngrx/store/testing";
import {AppState} from "../../../storage/app-state.reducer";

describe('PlayerComponent', () => {
  let component: PlayerComponent;
  let fixture: ComponentFixture<PlayerComponent>;
  let initialState: AppState = {
    authentication: null,
    party: {
      partyCode: null,
      downVotable: true,
      currentPlayback: {
        currentTrack: {
          albumName: "string",
          artists: [""],
          duration: 38383838,
          id: "string",
          imageHeight: 2,
          imageUrl: "",
          imageWidth: 2,
          name: "string",
          uri: "string"
        },
        playing: true,
        progressMs: 2
      },
      musicRequests: null
    }
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [
        provideMockStore({initialState})
      ],
      declarations: [PlayerComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlayerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
