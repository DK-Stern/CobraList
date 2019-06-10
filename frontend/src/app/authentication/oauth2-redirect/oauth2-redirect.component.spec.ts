import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {Oauth2RedirectComponent} from './oauth2-redirect.component';
import {MatCardModule} from '@angular/material';
import {ActivatedRoute} from '@angular/router';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {of} from 'rxjs';

describe('Oauth2RedirectComponent', () => {
  let component: Oauth2RedirectComponent;
  let fixture: ComponentFixture<Oauth2RedirectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MatCardModule,
        HttpClientTestingModule
      ],
      providers: [
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
