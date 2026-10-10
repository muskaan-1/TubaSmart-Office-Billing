import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { API_BASE } from './config';
export interface LoginResponse { token:string; role:string; }
@Injectable({providedIn:'root'}) export class AuthService {
 private http=inject(HttpClient); private base=API_BASE;
 login(username:string,password:string):Observable<LoginResponse>{return this.http.post<LoginResponse>(`${this.base}/auth/login`,{username,password}).pipe(tap(r=>{localStorage.setItem('token',r.token);localStorage.setItem('role',r.role);localStorage.setItem('username',username);}));}
 logout(){localStorage.clear();}
 isLoggedIn(){return !!localStorage.getItem('token');}
 token(){return localStorage.getItem('token');}
}
