import { Injectable } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Order } from './order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private ordersApiUrl = 'http://localhost:8080/orders';
  public ordersInProcess: Order[] = [];
  public ordersProcessed: Order[] = [];

  constructor(
    private http: HttpClient
  ) { }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  getOrders(): Observable<Order[]> {
    return this.http.get<{ _embedded: { orderList: Order[] } }>(this.ordersApiUrl).pipe(
      map(response => response?._embedded?.orderList),
      catchError(this.handleError<Order[]>([]))
    );
  }

  getOrder(id: number): Observable<Order> {
    return this.http.get<Order>(this.ordersApiUrl + "/" + id).pipe(
      catchError(this.handleError<Order>())
    );
  }

  deleteOrder(id: number): Observable<Order> {
    return this.http.delete<Order>(this.ordersApiUrl + "/" + id, this.httpOptions).pipe(
      catchError(this.handleError<Order>())
    );
  }

  addOrder(ids: number[]): Observable<Order> {
    return this.http.post<Order>(this.ordersApiUrl, ids, this.httpOptions).pipe(
      catchError(this.handleError<Order>())
    );
  }
}
