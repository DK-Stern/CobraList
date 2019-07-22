import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {Oauth2RedirectComponent} from './oauth2-redirect.component';
import {MatCardModule, MatProgressSpinnerModule} from '@angular/material';
import {ActivatedRoute, Router} from '@angular/router';
import {MockStore, provideMockStore} from '@ngrx/store/testing';
import {ActivedRouteStub} from '../../testing/actived-route-stub';
import {Store} from '@ngrx/store';
import {AppState} from '../../storage/app-state.reducer';
import {loginSuccess} from '../store/auth.actions';
import createSpyObj = jasmine.createSpyObj;

describe('Oauth2RedirectComponent', () => {
  let component: Oauth2RedirectComponent;
  let fixture: ComponentFixture<Oauth2RedirectComponent>;

  const initialState = {
    authentication: {
      isAuthenticated: false,
      isGuest: false,
      token: null,
      user: null,
      error: null
    },
    party: null
  };

  let store: MockStore<AppState>;
  let activatedRouteStub: ActivedRouteStub;
  let routerStub: Router;

  beforeEach(async(() => {
    activatedRouteStub = new ActivedRouteStub({token: null});

    routerStub = createSpyObj('Router', ['navigateByUrl']);

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
        },
        {
          provide: Router,
          useValue: routerStub
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

  it('should dispatch loginSuccess action with token if token is in queryParam', () => {
    // given
    const params = {token: '123'};
    activatedRouteStub.setQueryParams(params);

    // then
    expect(store.dispatch).toHaveBeenCalledWith(loginSuccess(params));
  });

  it('should not dispatch loginSuccess action if error is in queryParam', () => {
    // given
    const error = {error: 'error msg'};
    activatedRouteStub.setQueryParams(error);

    // then
    expect(store.dispatch).toHaveBeenCalledTimes(0);
  });

  it('should redirect of user is logged in', () => {
    // given
    store.setState({
      authentication: {
        isAuthenticated: false,
        isGuest: false,
        token: null,
        error: null,
        user: {
          name: 'Max',
          authorities: [],
          email: '',
          id: 1
        }
      },
      party: null
    });

    // when
    fixture.detectChanges();

    // then
    const routerSpy = routerStub.navigateByUrl as jasmine.Spy;
    const navArgs = routerSpy.calls.first().args[0];
    expect(navArgs).toBe('user', 'should nav to dashboard');
  });
});
