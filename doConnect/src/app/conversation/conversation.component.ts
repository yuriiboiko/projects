import { Component,OnInit, Input  } from '@angular/core';
import { ChatService } from '../service/chatService';
import { SharedService } from '../service/shared.service';


@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.css']
})
export class ConversationComponent implements OnInit {
  maxChatLength=255;
  messagesCurrent!: any[];
  messagesOther!: any[];
  messageText: string = '';
  currentUser!:string;
  otherUser!:string;

  sortedMessages!: any[];

  constructor(private sharedService: SharedService,private chatService: ChatService) {}


  ngOnInit(): void {
    //assign data using a service 
    this.sharedService.currentMessages.subscribe(messagesCurrent => {
      this.messagesCurrent = messagesCurrent.sort((a, b) => new Date(a.datetime).getTime() - new Date(b.datetime).getTime());
      this.currentUser=sessionStorage.getItem('username')??'';
    });
    this.sharedService.otherMessages.subscribe(messagesOther => {
      this.messagesOther = messagesOther.sort((a, b) => new Date(a.datetime).getTime() - new Date(b.datetime).getTime());
      this.otherUser=sessionStorage.getItem('otherUsername')??''
    });

    const combinedMessages = this.messagesCurrent.concat(this.messagesOther);
    this.sortedMessages=combinedMessages.sort((a, b) => new Date(a.datetime).getTime() - new Date(b.datetime).getTime());
  }
  
  sendMessage() {
    if (this.messageText) {
      this.chatService.addMessage(this.currentUser, this.messageText, this.otherUser).subscribe(() => {
        const newMessage = { 
          message: this.messageText, 
          datetime: new Date().toISOString(), 
          from_user: this.currentUser 
        };
        this.messageText = '';
        this.sortedMessages.push(newMessage);
        this.sortedMessages = this.sortedMessages.sort((a, b) => new Date(a.datetime).getTime() - new Date(b.datetime).getTime());
      });
    }
  }


}

