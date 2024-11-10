import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateBurgerFormComponent } from './create-burger-form.component';

describe('CreateBurgerFormComponent', () => {
  let component: CreateBurgerFormComponent;
  let fixture: ComponentFixture<CreateBurgerFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateBurgerFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateBurgerFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
