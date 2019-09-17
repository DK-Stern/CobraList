import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PartyInformationComponent} from './party-information.component';
import {Component} from "@angular/core";

describe('PartyInformationComponent', () => {
  let component: PartyInformationComponent;
  let fixture: ComponentFixture<PartyInformationComponent>;

  @Component({
    selector: 'app-music-requests',
    template: '<p>Mock Music Request Component</p>'
  })
  class MockMusicRequestComponent {
  }

  @Component({
    selector: 'app-player',
    template: '<p>Mock Player Component</p>'
  })
  class MockPlayerComponent {
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        PartyInformationComponent,
        MockMusicRequestComponent,
        MockPlayerComponent
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PartyInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
