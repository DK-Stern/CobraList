import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {Oauth2RedirectComponent} from './oauth2-redirect.component';
import {MatCardModule, MatProgressSpinnerModule} from '@angular/material';
import {ActivatedRoute} from '@angular/router';
import {MockStore, provideMockStore} from '@ngrx/store/testing';
import {ActivedRouteStub} from '../../testing/actived-route-stub';
import {Store} from '@ngrx/store';
import {AppState} from '../../storage/appStateReducer';
import {loggedIn} from '../auth.actions';

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

  let store: MockStore<AppState>;
  let activatedRouteStub: ActivedRouteStub;

  beforeEach(async(() => {
    activatedRouteStub = new ActivedRouteStub({token: null});

    TestBed.configureTestingModule({
      imports: [
        MatCardModule,
        MatProgressSpinnerModule
      ],
      providers: [
        provideMockStore({initialState}),
        {
          provide: ActivatedRoute,
          useValue: activatedRouteStub,
        }
      ],
      declarations: [Oauth2RedirectComponent]
    })
      .compileComponents();

    store = TestBed.get(Store);
    spyOn(store, 'dispatch').and.callThrough();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Oauth2RedirectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should dispatch loggedIn action with token if token is in queryParam', () => {
    // given
    const params = {token: '123'};
    activatedRouteStub.setQueryParams(params);

    // then
    expect(store.dispatch).toHaveBeenCalledWith(loggedIn(params));
  });

  it('should not dispatch loggedIn action if error is in queryParam', () => {
    // given
    const error = {error: 'error msg'};
    activatedRouteStub.setQueryParams(error);

    // then
    expect(store.dispatch).toHaveBeenCalledTimes(0);
  });
});
