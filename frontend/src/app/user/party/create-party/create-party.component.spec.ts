import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CreatePartyComponent} from './create-party.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatCardModule, MatCheckboxModule, MatFormFieldModule, MatInputModule, MatSelectModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {of} from 'rxjs';
import {ActivatedRoute} from '@angular/router';

describe('CreatePartyComponent', () => {
  let component: CreatePartyComponent;
  let fixture: ComponentFixture<CreatePartyComponent>;
  let activatedRouteStub;

  beforeEach(async(() => {
    activatedRouteStub = class {
      data;
    };
    activatedRouteStub.data = of({basePlaylists: []});

    TestBed.configureTestingModule({
      declarations: [
        CreatePartyComponent
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: activatedRouteStub
        }
      ],
      imports: [
        MatCardModule,
        FormsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatCheckboxModule,
        MatSelectModule,
        BrowserAnimationsModule
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreatePartyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
