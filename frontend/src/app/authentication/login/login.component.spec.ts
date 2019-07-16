import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {SessionTimedOutRedirectService} from '../oauth2-redirect/session-timed-out-redirect.service';
import {MatCardModule} from '@angular/material';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    let loginRediectService: SessionTimedOutRedirectService = new SessionTimedOutRedirectService;
    let redirectLoginSpy = spyOn(loginRediectService, 'redirectLogin');
    TestBed.configureTestingModule({
      providers: [{
        provide: SessionTimedOutRedirectService,
        useValuer: redirectLoginSpy
      }
      ],
      declarations: [LoginComponent],
      imports: [MatCardModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
