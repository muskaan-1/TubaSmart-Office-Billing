import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE } from './config';
@Injectable({providedIn:'root'}) export class ApiService {
 private http=inject(HttpClient); private base=API_BASE;
 get<T>(path:string):Observable<T>{return this.http.get<T>(this.base+path)}
 post<T>(path:string,data:any={}):Observable<T>{return this.http.post<T>(this.base+path,data)}
 put<T>(path:string,data:any):Observable<T>{return this.http.put<T>(this.base+path,data)}
 delete(path:string){return this.http.delete(this.base+path)}
}
