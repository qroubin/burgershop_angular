import { Injectable } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Burger } from './burger';

@Injectable({
  providedIn: 'root'
})
export class BurgerService {

  public burgersApiUrl = 'http://localhost:8080/burgers';
  public burgers: Burger[] = [];
  public selectedBurgerIds: Set<number> = new Set();

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

  getBurgers(): Observable<Burger[]> {
    return this.http.get<{ _embedded: { burgerList: Burger[] } }>(this.burgersApiUrl).pipe(
      map(response => response?._embedded?.burgerList),
      catchError(this.handleError<Burger[]>([]))
    );
  }

  getBurger(id: number): Observable<Burger> {
    return this.http.get<Burger>(this.burgersApiUrl + "/" + id).pipe(
      catchError(this.handleError<Burger>())
    );
  }

  deleteBurger(id: number): Observable<Burger> {
    return this.http.delete<Burger>(this.burgersApiUrl + "/" + id, this.httpOptions)
  }

  addBurger(burger: Burger): Observable<Burger> {
    return this.http.post<Burger>(this.burgersApiUrl, burger,this.httpOptions).pipe(
      catchError(this.handleError<Burger>())
    );
  }
}
