import { Component } from '@angular/core';
import { BurgerService } from '../burger.service';
import { Burger } from '../burger';

@Component({
  selector: 'app-create-burger-form',
  standalone: true,
  imports: [],
  templateUrl: './create-burger-form.component.html',
  styleUrl: './create-burger-form.component.css'
})
export class CreateBurgerFormComponent {

  constructor(private burgerService: BurgerService) { }

  title = "Creez votre burger!";

  onAddBurgerButton(burgerName: String, burgerIngredients: String): void {
    const name : String = burgerName.trim();
    const ingredients : String[] = burgerIngredients.trim().split(",");
    if(!name || !burgerIngredients.trim()) return;
    this.burgerService.addBurger({name, ingredients} as Burger)
      .subscribe(burger => {
        this.burgerService.burgers.push(burger);
      });
  }

}
