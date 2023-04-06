import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SharedService {
  private messagesCurrent = new BehaviorSubject<any[]>([]);
  private messagesOther = new BehaviorSubject<any[]>([]);

  currentMessages = this.messagesCurrent.asObservable();
  otherMessages = this.messagesOther.asObservable();

  constructor() { }

  changeMessages(messagesCurrent: any[], messagesOther: any[]) {
    this.messagesCurrent.next(messagesCurrent);
    this.messagesOther.next(messagesOther);
  }
}