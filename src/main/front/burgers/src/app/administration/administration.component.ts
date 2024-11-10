import { Component } from '@angular/core';
import { BurgerListComponent } from '../burger-list/burger-list.component';
import { CreateBurgerFormComponent } from '../create-burger-form/create-burger-form.component';

@Component({
  selector: 'app-administration',
  standalone: true,
  imports: [BurgerListComponent, CreateBurgerFormComponent],
  templateUrl: './administration.component.html',
  styleUrl: './administration.component.css'
})
export class AdministrationComponent {
  title = 'Administration'
}
