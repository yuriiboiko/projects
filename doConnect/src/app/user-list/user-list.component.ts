import { Component ,OnInit} from '@angular/core';
import { ChatService } from '../service/chatService';
import { Router } from '@angular/router';
import { SharedService } from '../service/shared.service';


@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users!: any[];


  constructor(private chatService: ChatService,private router: Router,private sharedService: SharedService) {
  }

  ngOnInit(): void {

    const username=sessionStorage.getItem("username")??'';
    this.chatService.getAllExceptMe(username).subscribe(user => {
      this.users = user;
    });


  }


  displayMessages(user: any): void {
    var user1= sessionStorage.getItem('username')??'';
    sessionStorage.setItem("otherUsername", user.username);
    var user2= user.username;
    this.chatService.getMessages(user1,user2).subscribe(response => {
      const messagesCurrent = response;
      this.chatService.getMessages(user2,user1).subscribe(response => {
        const messagesOther = response;
        this.sharedService.changeMessages(messagesCurrent, messagesOther);
        this.router.navigate(['conversation']);
      });
    });
  }
}