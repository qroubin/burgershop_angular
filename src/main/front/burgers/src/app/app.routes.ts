import { Routes } from '@angular/router';
import { AdministrationComponent } from './administration/administration.component';
import { OrdersComponent } from './orders/orders.component';

export const routes: Routes = [
    { path: 'administration', component: AdministrationComponent},
    { path: 'commandes', component: OrdersComponent},
    { path: '', redirectTo: '/administration', pathMatch: 'full' },
    { path: '**', redirectTo: '/administration' }
];
