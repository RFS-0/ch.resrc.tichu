import { CreateGameEvent, Game } from '@/domain/entities/game';
import { CreateGame } from '@/endpoints/endpoints';
import { Observable, Subject } from 'rxjs';

export class CreateGameWebSocket implements CreateGame {
  constructor(private requestUrl: string, private responseUrl: string) {}

  send(intent: CreateGameEvent): Observable<Game> {
    console.log("sending create game event to backend");
    const response = new Subject<Game>();
    const requestSocket = new WebSocket(this.requestUrl);
    const responseSocket = new WebSocket(`${this.responseUrl}/${intent.createdBy}`);
    requestSocket.onopen = (event: Event) => {
      requestSocket.send(intent.toDto());
      requestSocket.close();
    };
    requestSocket.onerror = (event) => {
      requestSocket.close();
    };
    responseSocket.onerror = (event) => {
      requestSocket.close();
    };
    responseSocket.onmessage = (event: MessageEvent) => {
      response.next(Game.fromJson(event.data));
      response.complete();
    };
    return response.asObservable();
  }
}
