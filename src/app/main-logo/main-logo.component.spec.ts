import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MainLogoComponent } from './main-logo.component';

describe('MainLogoComponent', () => {
  let component: MainLogoComponent;
  let fixture: ComponentFixture<MainLogoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MainLogoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MainLogoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
