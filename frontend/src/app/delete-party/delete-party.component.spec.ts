import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DeletePartyComponent} from './delete-party.component';
import {provideMockStore} from "@ngrx/store/testing";
import {DeletePartyService} from "./delete-party.service";
import {of} from "rxjs";
import {empty} from "rxjs/internal/Observer";
import {Router} from "@angular/router";

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

    let routerStub = jasmine.createSpyObj('Router', ['navigateByUrl']);

    TestBed.configureTestingModule({
      providers: [
        provideMockStore({initialState}),
        {
          provide: DeletePartyService,
          useValue: deletePartyServiceSpy
        },
        {
          provide: Router,
          useValue: routerStub
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
