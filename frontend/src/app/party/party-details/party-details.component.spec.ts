import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PartyDetailsComponent} from './party-details.component';
import {ActivatedRoute} from '@angular/router';
import {of} from 'rxjs';
import {PartyValueObject} from '../party.value-object';

describe('PartyDetailsComponent', () => {
  let component: PartyDetailsComponent;
  let fixture: ComponentFixture<PartyDetailsComponent>;
  let activatedRouteStub;

  beforeEach(async(() => {
    let partyObject: PartyValueObject = {
      id: "1",
      name: "",
      downVoting: true,
      password: ""
    };

    activatedRouteStub = class {
      data;
    };
    activatedRouteStub.data = of([partyObject]);

    TestBed.configureTestingModule({
      declarations: [PartyDetailsComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: activatedRouteStub
        }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PartyDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
