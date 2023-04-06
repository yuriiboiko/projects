import { Component,OnInit  } from '@angular/core';
import { Router } from '@angular/router';
import { JwtRequest } from '../model/jwtRequest';
import { LoginService } from '../service/loginService';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  user: JwtRequest= new JwtRequest();

  constructor(private router:Router, private loginService: LoginService){}
  
  ngOnInit(): void {}
  

  formSubmit() {
    this.loginService.authenticate(this.user).subscribe({
      next: response => {
        // Login successful, 
        alert("Login Successfull");

        //set session storage
        this.assignSessionStorage(this.user.username)

        //navigate to the main page
        this.router.navigate(['main']);
      },
      error: error => {
        // Login failed, display error message
        alert("Login Unsuccessfull");
        console.log('Login error:', error);
      }
    });
  }



  assignSessionStorage(username:string){
    this.loginService.getUserInfo(username).subscribe({
      next: response=>{
        sessionStorage.setItem("role",response.role);
        sessionStorage.setItem("email", response.email);
      },error: error=>{
        console.log('Login error:', error);
      }
    });

  }

  goToRegisterPage() {
    this.router.navigate(['register']);

  }
  goToHomePage(){
    this.router.navigate(['']);
  }

  goToMainPage(){
    this.router.navigate(['main']);
  }



}
