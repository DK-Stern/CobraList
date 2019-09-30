import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SearchMusicRequestComponent} from './search-music-request.component';
import {of} from "rxjs";
import {ActivatedRoute, convertToParamMap} from "@angular/router";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {SearchMusicRequestService} from "./search-music-request.service";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import createSpyObj = jasmine.createSpyObj;

describe('SearchMusicRequestComponent', () => {
  let component: SearchMusicRequestComponent;
  let fixture: ComponentFixture<SearchMusicRequestComponent>;
  let activatedRouteStub;

  beforeEach(async(() => {
    activatedRouteStub = class {
    };
    activatedRouteStub.paramMap = of(convertToParamMap({id: "partyCode"}));
    const findPartyServiceSpy = createSpyObj('SearchMusicRequestService', ['searchMusicRequest', 'addMusicRequest']);

    TestBed.configureTestingModule({
      providers: [
        {
          provide: ActivatedRoute,
          useValue: activatedRouteStub
        },
        {
          provide: SearchMusicRequestService,
          useValue: findPartyServiceSpy
        }
      ],
      imports: [
        HttpClientTestingModule,
        MatAutocompleteModule,
        MatInputModule,
        ReactiveFormsModule,
        NoopAnimationsModule,
        FormsModule
      ],
      declarations: [SearchMusicRequestComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchMusicRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
