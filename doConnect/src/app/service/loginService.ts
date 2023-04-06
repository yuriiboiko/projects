import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { JwtRequest } from "../model/jwtRequest";
import { Token } from "../model/Token";
import { map } from "rxjs/operators";
import { USerDao } from "../model/UserDao";

@Injectable({
    providedIn:"root"
})
export class LoginService{


    private baseUrl = "http://localhost:8080";


    constructor(private httpClient : HttpClient){}

    getUserInfo(username:string):Observable<USerDao>{
      return this.httpClient.get(`${this.baseUrl}/getUserByUsername/${username}`) as Observable<USerDao>;
    }


    authenticate(user : JwtRequest) {
      
        return this.httpClient.post<any>(`${this.baseUrl}/authenticate`, user).pipe(
            map(userData => {
              sessionStorage.setItem("username", user.username);
              let tokenStr = "Bearer " + userData.token;
              sessionStorage.setItem("token", tokenStr);
              return userData;
            })
          );
      }

      registrateUser(user : JwtRequest):Observable<Object>{
        return this.httpClient.post(`${this.baseUrl}`, user)
      }
    

      isUserLoggedIn() {
        let token = sessionStorage.getItem("token");
        if((token === null) ){
          return false
        }
        return true;
      }
    
      logOut() {
        sessionStorage.removeItem("token");
      }
      

}
