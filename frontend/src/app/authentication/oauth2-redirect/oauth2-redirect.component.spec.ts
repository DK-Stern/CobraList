import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {Oauth2RedirectComponent} from './oauth2-redirect.component';
import {MatCardModule, MatProgressSpinnerModule} from '@angular/material';
import {ActivatedRoute} from '@angular/router';
import {of} from 'rxjs';
import {provideMockStore} from '@ngrx/store/testing';

describe('Oauth2RedirectComponent', () => {
  let component: Oauth2RedirectComponent;
  let fixture: ComponentFixture<Oauth2RedirectComponent>;
  const initialState = {
    authState: {
      isAuthenticated: false,
      token: null,
      user: null
    }
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MatCardModule,
        MatProgressSpinnerModule
      ],
      providers: [
        provideMockStore({initialState}),
        {
          provide: ActivatedRoute,
          useValue: {
            queryParams: of([{token: 1}]),
          },
        }
      ],
      declarations: [Oauth2RedirectComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Oauth2RedirectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
