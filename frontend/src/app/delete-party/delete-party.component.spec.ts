import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DeletePartyComponent} from './delete-party.component';
import {RouterTestingModule} from "@angular/router/testing";
import {provideMockStore} from "@ngrx/store/testing";
import {DeletePartyService} from "./delete-party.service";
import {of} from "rxjs";
import {empty} from "rxjs/internal/Observer";

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
    party: {
      partyCode: '123456',
      downVotable: null,
      currentPlayback: null,
      musicRequests: null
    }
  };

  beforeEach(async(() => {
    let deletePartyServiceSpy = jasmine.createSpyObj('DeletePartyService', ['deleteParty']);
    deletePartyServiceSpy.deleteParty.withArgs('123456').and.returnValue(of(empty));

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
