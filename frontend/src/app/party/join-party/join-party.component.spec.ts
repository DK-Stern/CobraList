import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {JoinPartyComponent} from './join-party.component';
import {MatCardModule, MatFormFieldModule, MatIconModule, MatInputModule, MatStepperModule} from '@angular/material';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {JoinPartyService} from './join-party.service';
import {FindPartyService} from '../find-party/find-party.service';
import {RouterTestingModule} from "@angular/router/testing";
import {AppState} from "../../storage/app-state.reducer";
import {provideMockStore} from "@ngrx/store/testing";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import createSpyObj = jasmine.createSpyObj;

describe('JoinPartyComponent', () => {
  let component: JoinPartyComponent;
  let fixture: ComponentFixture<JoinPartyComponent>;

  const initialState: AppState = {
    authentication: null,
    party: null
  };

  beforeEach(async(() => {
    const joinPartyServiceSpy = createSpyObj('JoinPartyService', ['joinParty']);
    const findPartyServiceSpy = createSpyObj('FindPartyService', ['findParty']);

    TestBed.configureTestingModule({
      declarations: [JoinPartyComponent],
      imports: [
        MatCardModule,
        FormsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        BrowserAnimationsModule,
        MatStepperModule,
        MatIconModule,
        RouterTestingModule,
        MatSnackBarModule
      ],
      providers: [
        provideMockStore({initialState}),
        {
          provide: JoinPartyService,
          useValue: joinPartyServiceSpy
        },
        {
          provide: FindPartyService,
          useValue: findPartyServiceSpy
        }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JoinPartyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
