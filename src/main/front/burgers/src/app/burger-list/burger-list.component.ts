import { Component, Input} from '@angular/core';
import { Burger } from '../burger';
import { FormsModule } from '@angular/forms'; //NgModel is here
import { NgFor, NgIf } from '@angular/common';
import { BurgerService } from '../burger.service';

@Component({
  selector: 'app-burger-list',
  standalone: true,
  imports: [FormsModule, NgFor, NgIf],
  templateUrl: './burger-list.component.html',
  styleUrl: './burger-list.component.css'
})
export class BurgerListComponent {
  constructor(public burgerService: BurgerService) { }
  title = "Liste des burgers";
  errorMsgUnderBurger: Map<number, string> = new Map<number, string>();
  errorTimeouts: Map<number, any> = new Map<number, any>();
  burger?: Burger;
  @Input() burgerListButtonLabel: string = 'Supprimer la sélection';
  @Input() burgerListButtonHandler: () => void = this.onDeleteBurgersButton;
  
  setupTimedErrorMsgUnderBurgers(id:number, msg: string, millis: number){
    this.errorMsgUnderBurger.set(id, msg);
    this.errorTimeouts.set(id, setTimeout(() => {
      this.errorMsgUnderBurger.delete(id);
      this.errorTimeouts.delete(id);
    }, millis));
  }

  toggleSelectedBurgerId(burgerId: number): void {
    if(this.burgerService.selectedBurgerIds.has(burgerId)){
      this.burgerService.selectedBurgerIds.delete(burgerId);
    } else {
      this.burgerService.selectedBurgerIds.add(burgerId);
    }
  }

  isBurgerChecked(burgerId: number): boolean {
    return this.burgerService.selectedBurgerIds.has(burgerId);
  }

  onDeleteBurgersButton(): void {
    this.burgerService.selectedBurgerIds.forEach(burgerId => this.burgerService.deleteBurger(burgerId)
      .subscribe({
        next: (response) => {
          if(response !== undefined){
            this.burgerService.burgers = this.burgerService.burgers.filter(b => b.id != burgerId);
          }
        },
        error: (err) => {
          if (err.status === 409) {
            this.setupTimedErrorMsgUnderBurgers(burgerId, "Impossible de supprimer, ce burger est présent dans des commandes!", 3000);
          }
        }
      })
    );
    this.burgerService.selectedBurgerIds.clear();
  }

  getBurgers(): void {
    this.burgerService.getBurgers().subscribe(burgers => this.burgerService.burgers = burgers);
  }

  getBurger(id: number): void {
    this.burgerService.getBurger(id).subscribe(burger => this.burger = burger);
  }

  ngOnInit(): void {
    this.getBurgers();
  }

}
