import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PlayerComponent} from './player.component';
import {provideMockStore} from "@ngrx/store/testing";
import {AppState} from "../../../storage/app-state.reducer";
import {MatIconModule} from "@angular/material/icon";
import {UserApiService} from "../../../user/user-api.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import createSpyObj = jasmine.createSpyObj;

describe('PlayerComponent', () => {
  let component: PlayerComponent;
  let fixture: ComponentFixture<PlayerComponent>;
  let initialState: AppState = {
    authentication: {
      isGuest: false,
      isAuthenticated: true,
      error: null,
      token: null,
      user: null
    },
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
          imageUrl: "url",
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
    const userApiServiceSpy = createSpyObj('UserApiService', ['getUser']);
    TestBed.configureTestingModule({
      imports: [
        MatIconModule,
        HttpClientTestingModule
      ],
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
