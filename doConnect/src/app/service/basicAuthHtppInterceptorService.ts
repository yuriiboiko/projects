import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { LoginService } from './loginService'; 

@Injectable({
  providedIn: 'root'
})
export class BasicAuthHtppInterceptorService implements HttpInterceptor {

  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {

    if (sessionStorage.getItem('username') && sessionStorage.getItem('token')) {
        const token = sessionStorage.getItem('token') || ''; // Assign a default value if null
      req = req.clone({
        setHeaders: {
          Authorization: token
        }
      })
    }

    return next.handle(req);

  }
}