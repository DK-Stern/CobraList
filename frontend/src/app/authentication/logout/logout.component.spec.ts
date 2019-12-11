import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LogoutComponent} from './logout.component';
import {provideMockStore} from '@ngrx/store/testing';
import {AppState} from '../../storage/app-state.reducer';
import {LocalStorageService} from '../../storage/local-storage.service';
import {RouterTestingModule} from '@angular/router/testing';
import {MatCardModule} from '@angular/material';

describe('LogoutComponent', () => {
  let component: LogoutComponent;
  let fixture: ComponentFixture<LogoutComponent>;
  let initialState: AppState = {
    authentication: null,
    party: null
  };

  beforeEach(async(() => {
    let localStorageSpy = jasmine.createSpyObj('LocalStorageService', ['clearAll']);

    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatCardModule
      ],
      providers: [
        provideMockStore({initialState}),
        {
          provide: LocalStorageService,
          useValue: localStorageSpy
        }
      ],
      declarations: [LogoutComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LogoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
