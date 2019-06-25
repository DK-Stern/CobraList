import {async, TestBed} from '@angular/core/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AppComponent} from './app.component';
import {MatCardModule, MatToolbarModule} from '@angular/material';
import {MockStore, provideMockStore} from '@ngrx/store/testing';
import {AppState} from './storage/appStateReducer';
import {Store} from '@ngrx/store';
import {LocalStorageService, STORAGE_KEY} from './storage/local-storage.service';

describe('AppComponent', () => {

  let mockStore: MockStore<AppState>;
  let initialState = {
    authState: {
      isAuthenticated: false,
      token: null,
      user: null,
      error: null
    }
  };
  let localStorageServiceLoadItemSpy;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatToolbarModule,
        MatCardModule
      ],
      providers: [
        LocalStorageService,
        provideMockStore({initialState})
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    const localStorageService = TestBed.get(LocalStorageService);
    localStorageServiceLoadItemSpy = spyOn(localStorageService, 'loadItem').withArgs(STORAGE_KEY.USER).and.returnValue(null);
    spyOn(localStorageService, 'saveItem');

    mockStore = TestBed.get(Store);
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'CobraList'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('CobraList');
  });

  it('should show user on nav if user is loaded', () => {
    // given
    const fixture = TestBed.createComponent(AppComponent);
    const expectedName = 'Max';

    mockStore.setState({
      authState: {
        isAuthenticated: true,
        token: '123',
        error: null,
        user: {
          authorities: ['USER'],
          email: 'email@mail.de',
          id: 1,
          name: expectedName
        }
      }
    });

    // when
    fixture.detectChanges();

    // then
    const userEl = fixture.nativeElement.querySelector('#user-nav');
    expect(userEl.textContent).toEqual(`Willkommen ${expectedName}!`);
    const loginEl = fixture.nativeElement.querySelector('#login');
    expect(loginEl).toBeNull();
  });

  it('should show login on nav if no user found', () => {
    // given
    const fixture = TestBed.createComponent(AppComponent);
    mockStore.setState(initialState);

    // when
    fixture.detectChanges();

    // then
    const loginEl = fixture.nativeElement.querySelector('#login');
    expect(loginEl.textContent).toEqual('Login mit Spotify');
    const userEl = fixture.nativeElement.querySelector('#user-nav');
    expect(userEl).toBeNull();
  });

  it('should load user from store', () => {
    // given
    const expectedName = 'peter';
    const fixture = TestBed.createComponent(AppComponent);

    let state: AppState = {
        authState: {
          isAuthenticated: false,
          token: null,
          error: null,
          user: {
            id: 1,
            name: expectedName,
            email: 'email@email.de',
            authorities: ['USER']
          }
        }
      };
    mockStore.setState(state);
    let dispatchSpy = spyOn(mockStore, 'dispatch');

    // when
    fixture.detectChanges();

    // then
    const userEl = fixture.nativeElement.querySelector('#user-nav');
    expect(userEl.textContent).toEqual(`Willkommen ${expectedName}!`);
    expect(dispatchSpy).toHaveBeenCalledTimes(0);
  });

  // it('should load user from LocalStorageService', () => {
  //   // given
  //   const fixture = TestBed.createComponent(AppComponent);
  //   const expectedName = 'peter';
  //   let user = {
  //     id: 1,
  //     name: expectedName,
  //     email: 'email@email.de',
  //     authorities: ['USER']
  //   };
  //   localStorageServiceLoadItemSpy.withArgs(STORAGE_KEY.USER).and.returnValue(user);
  //   let dispatchSpy = spyOn(mockStore, 'dispatch');
  //
  //   // when
  //   fixture.detectChanges();
  //
  //   // then
  //   expect(dispatchSpy).toHaveBeenCalledWith(loadedUser({user: user}));
  // });
});
