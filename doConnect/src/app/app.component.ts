import { Component, OnInit } from '@angular/core';
import { LoginService } from './service/loginService';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'doConnectFrontend';
  
  username=sessionStorage.getItem("username");

  topicList = [
      'Math',
      'Science',
      'History',
      'Geography',
      'Literature',
      'Art',
      'Music',
      'Technology',
      'Philosophy',
      'Psychology',
      'Sociology',
      'Economics',
      'Politics',
      'Law',
      'Medicine',
      'Nutrition',
      'Fitness',
      'Environment',
      'Animals',
      'Fashion'
    ];
  

  constructor( private loginService: LoginService){
    sessionStorage.setItem("topicList", JSON.stringify(this.topicList));

  }

  checkLoginStatus(){
    return this.loginService.isUserLoggedIn();
  }

  checkAdmin(){
    if(sessionStorage.getItem("role")!=="admin"){
      return false;
    }
    return true;
  }
  logMeOut(){
    this.loginService.logOut();
  }
  
  ngOnInit(): void {}



}
