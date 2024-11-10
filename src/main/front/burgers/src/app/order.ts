import { Burger } from "./burger";

export interface Order {
    id: number;
    burgers: Burger[];
    isProcessed: Boolean;
}