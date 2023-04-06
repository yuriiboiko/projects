import { Component ,OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { USerDao } from '../model/UserDao';
import { RegistrateService } from '../service/registrate.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  user: USerDao= new USerDao();
  verifypassword:string | undefined;
  passwordsDoNotMatch = false;

  constructor(private router:Router,private registerService: RegistrateService) {

  }
  ngOnInit(): void {}


  validatePasswords(){
    if (this.user.password!== this.verifypassword) {
      this.passwordsDoNotMatch = true;
      return;
    }
    this.passwordsDoNotMatch = false;

  }


  formSubmit(){
    this.validatePasswords();
    if(this.passwordsDoNotMatch){
      alert("Passwords do not match!");
      return;
    }

    this.registerService.registrateUser(this.user).subscribe({
      next: (data) => {
        alert("Register Successfully");
        this.goToLoginPage();
      },
      error: (err) => {
        alert("Missing fields or Username already exists");
    }
    });

  }



  goToHomePage(){
    this.router.navigate(['']);
  }
  
  goToLoginPage(){
    this.router.navigate(['login']);
  }
}
