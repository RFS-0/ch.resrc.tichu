import { AddFirstPlayerToTeam } from '@/endpoints/endpoints';
import { Game } from '@/domain/entities/game';
import { Observable, Subject } from 'rxjs';
import { AddPlayerEvent } from '@/domain/entities/team';

export class AddFirstPlayerToTeamWebSocket implements AddFirstPlayerToTeam {
  constructor(private requestUrl: string, private responseUrl: string) {
  }

  send(intent: AddPlayerEvent): Observable<Game> {
    const response = new Subject<Game>();
    const requestSocket = new WebSocket(this.requestUrl);
    const responseSocket = new WebSocket(`${this.responseUrl}/${intent.gameId}`);
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
