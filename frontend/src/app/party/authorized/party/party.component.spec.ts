import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PartyComponent} from './party.component';
import {RouterTestingModule} from '@angular/router/testing';
import {AppState} from "../../../storage/app-state.reducer";
import {provideMockStore} from "@ngrx/store/testing";

describe('PartyComponent', () => {
  let component: PartyComponent;
  let fixture: ComponentFixture<PartyComponent>;

  let initialState: AppState = {
    authentication: null,
    party: null
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PartyComponent],
      providers: [
        provideMockStore({initialState})
      ],
      imports: [
        RouterTestingModule
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PartyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
