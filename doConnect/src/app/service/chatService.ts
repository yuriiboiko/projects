import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn:"root"
})
export class ChatService{

    private apiUrl = 'http://localhost:8080';
    
    private getAllUsersUrl = `${this.apiUrl}/getAll`;
    private addMsgUrl = `${this.apiUrl}/chat/addmsg`;
    private getMessagesUrl = `${this.apiUrl}/chat/getmessages`;
  


    constructor(private httpClient: HttpClient) {}

    getAllUsers(): Observable<any[]> {
    return this.httpClient.get<any[]>(this.getAllUsersUrl);
    }

    getAllExceptMe(username:string): Observable<any[]> {
      const url = `${this.apiUrl}/getAllBesidesMe/${username}`;
      console.log(url)
      return this.httpClient.get<any[]>(url);
    }

    

    addMessage(fromUser: string, message: string, toUser: string) {
    const body = {
      from_user: fromUser,
      message: message,
      to_user: toUser
    };
    return this.httpClient.post(this.addMsgUrl, body);
  }


  getMessages(fromUser: string, toUser: string):Observable<any[]> {
    const url = `${this.getMessagesUrl}/${fromUser}/${toUser}`;
    console.log(url);
    console.log(this.httpClient.get<any[]>(url));
    return this.httpClient.get<any[]>(url);
  }

}