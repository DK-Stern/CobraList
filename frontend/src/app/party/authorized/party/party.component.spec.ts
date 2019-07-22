import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PartyComponent} from './party.component';
import {RouterTestingModule} from '@angular/router/testing';

describe('PartyComponent', () => {
  let component: PartyComponent;
  let fixture: ComponentFixture<PartyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PartyComponent],
      imports: [RouterTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PartyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
