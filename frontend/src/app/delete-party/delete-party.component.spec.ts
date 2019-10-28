import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DeletePartyComponent} from './delete-party.component';
import {RouterTestingModule} from "@angular/router/testing";
import {provideMockStore} from "@ngrx/store/testing";
import {DeletePartyService} from "./delete-party.service";

describe('DeletePartyComponent', () => {
  let component: DeletePartyComponent;
  let fixture: ComponentFixture<DeletePartyComponent>;

  let initialState = {
    authentication: {
      isAuthenticated: false,
      isGuest: false,
      token: null,
      user: null,
      error: null
    },
    party: null
  };

  beforeEach(async(() => {
    let deletePartyServiceSpy = jasmine.createSpyObj('DeletePartyService', ['deleteParty']);

    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule
      ],
      providers: [
        provideMockStore({initialState}),
        {
          provide: DeletePartyService,
          useValue: deletePartyServiceSpy
        }
      ],
      declarations: [DeletePartyComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeletePartyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
