import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {MusicRequestsComponent} from './music-requests.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {provideMockStore} from "@ngrx/store/testing";
import {AppState} from "../../../storage/app-state.reducer";
import {MatTableModule} from "@angular/material/table";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

describe('MusicRequestsComponent', () => {
  let component: MusicRequestsComponent;
  let fixture: ComponentFixture<MusicRequestsComponent>;
  let initialState: AppState = {
    authentication: null,
    party: {
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
      imports: [
        MatFormFieldModule,
        MatInputModule,
        MatTableModule,
        BrowserAnimationsModule
      ],
      providers: [
        provideMockStore({initialState})
      ],
      declarations: [MusicRequestsComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MusicRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
