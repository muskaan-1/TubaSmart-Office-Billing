import {Component} from '@angular/core';
import {RouterOutlet,Router} from '@angular/router';
import {NgIf} from '@angular/common';
import {AuthService} from './core/auth.service';
import {ShellComponent} from './layout/shell.component';
@Component({selector:'app-root',standalone:true,imports:[RouterOutlet,NgIf,ShellComponent],template:`<app-shell *ngIf="auth.isLoggedIn(); else publicPage"><router-outlet></router-outlet></app-shell><ng-template #publicPage><router-outlet></router-outlet></ng-template>`})
export class AppComponent{constructor(public auth:AuthService,private router:Router){}}
