import { Component } from '@angular/core';
import { BurgerListComponent } from '../burger-list/burger-list.component';
import { BurgerService } from '../burger.service';
import { OrderService } from '../order.service';
import { NgFor, NgIf } from '@angular/common';
import { Burger } from '../burger';
import { Subscription } from 'rxjs';
import { WebSocketService } from '../web-socket.service';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [BurgerListComponent, NgFor, NgIf],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent {
  title = 'Commandes'
  private orderWsSubscription: Subscription;
  private orderWsConnectionStatusSubscription: Subscription;

  constructor(private burgerService: BurgerService, public orderService: OrderService, private webSocketService: WebSocketService) { 
    this.orderWsSubscription = new Subscription;
    this.orderWsConnectionStatusSubscription = new Subscription;
  }

  onPostOrderButton = (): void => {
    if(this.burgerService.selectedBurgerIds.size == 0) return;
    this.orderService.addOrder(Array.from(this.burgerService.selectedBurgerIds)).subscribe(order => {
      if(order) this.orderService.ordersInProcess.push(order);
    });
    this.burgerService.selectedBurgerIds.clear();
  }

  getOrders(): void {
    this.orderService.getOrders().subscribe(orders => {
      if(orders) {
        this.orderService.ordersInProcess = [];
        this.orderService.ordersProcessed = [];
        orders.forEach(order => {
          if(!order.isProcessed) this.orderService.ordersInProcess.push(order);
          else this.orderService.ordersProcessed.push(order);
        });
      }
    });
  }

  getBurgersToString(burgers: Burger[]): string {
    return burgers.map(burger => burger.name + " (" + burger.ingredients + ')').join(', ');
  }

  ngOnInit(): void {
    this.getOrders();
    this.webSocketService.connect().subscribe(
      connected => {
        if(connected) {
          this.orderWsSubscription = this.webSocketService.subscribeToOrderWsUpdates().subscribe(
            newValue => this.getOrders()       //something can be done with the newValue, but we only call getOrders
          );
        } else {
          console.error('WebSocket connection failed');
        }
      }
    );
    
  }

  ngOnDestroy(): void {
    this.orderWsSubscription.unsubscribe();
    this.orderWsConnectionStatusSubscription.unsubscribe();
    this.webSocketService.disconnect();
  }

  updateOrderWs(newValue: string): void {
    this.webSocketService.updateOrderWs(newValue);
  }
  
}
