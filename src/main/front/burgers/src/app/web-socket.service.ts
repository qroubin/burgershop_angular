import { Injectable } from '@angular/core';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient!: Stomp.Client;
  private connectionStatus: Subject<boolean> = new Subject<boolean>();

  constructor() { }

  connect(): Observable<boolean> {
    const socket = SockJS('http://localhost:8080/ws');
    this.stompClient = Stomp.over(socket);
    return new Observable<boolean>(observer => {
      this.stompClient.connect({}, () => {
        console.log('WebSocket connected');
        this.connectionStatus.next(true);
        observer.next(true);
        observer.complete();
      }, error => {
        console.error('WebSocket connection failed: ' + error);
        observer.error(error);
      });
    });
  }

  disconnect(): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.disconnect(() => {
        console.log('disconnected from WebSocket server');
        this.connectionStatus.next(false);
      });
    }
  }

  getConnectionStatus(): Observable<boolean> {
    return this.connectionStatus.asObservable();
  }

  subscribeToOrderWsUpdates(): Observable<any> {
    return new Observable(observer => {
      this.stompClient.subscribe('/orderWs/value', message => {
        observer.next(message.body);
      });
    });
  }

  updateOrderWs(newValue: string): void {
    if(this.stompClient && this.stompClient.connected)
      this.stompClient.send('/app/updateOrderWs', {}, newValue);
    else
      console.error('WebSocket connection not established');
  }
}
