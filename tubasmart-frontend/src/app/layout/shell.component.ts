import {Component} from '@angular/core';
import {Router,RouterLink,RouterLinkActive,RouterOutlet} from '@angular/router';
import {NgFor} from '@angular/common';
import {AuthService} from '../core/auth.service';

@Component({selector:'app-shell',standalone:true,imports:[RouterLink,RouterLinkActive,RouterOutlet,NgFor],template:`
<div class="app">
  <aside class="sidebar" [class.open]="mobileOpen">
    <div class="sb-brand"><div class="logo-mark">TS</div><div><b>TubaSmart</b><small>Office Leasing Billing Solution</small></div></div>
    <nav class="sb-scroll">
      <div class="sb-group">Overview</div><a routerLink="/dashboard" routerLinkActive="active" (click)="mobileOpen=false"><span>▦</span>Dashboard</a>
      <div class="sb-group">Transactions</div>
      <a routerLink="/invoices" routerLinkActive="active" (click)="mobileOpen=false"><span>🧾</span>Invoices</a>
      <a routerLink="/billing" routerLinkActive="active" (click)="mobileOpen=false"><span>⟳</span>Monthly Billing Run</a>
      <a routerLink="/receipts" routerLinkActive="active" (click)="mobileOpen=false"><span>💵</span>Receipts</a>
      <div class="sb-group">Property Masters</div>
      <a *ngFor="let m of propertyMasters" [routerLink]="['/masters',m.key]" routerLinkActive="active" (click)="mobileOpen=false"><span>{{m.icon}}</span>{{m.label}}</a>
      <div class="sb-group">Billing Masters</div>
      <a *ngFor="let m of billingMasters" [routerLink]="['/masters',m.key]" routerLinkActive="active" (click)="mobileOpen=false"><span>{{m.icon}}</span>{{m.label}}</a>
      <div class="sb-group">Reports</div>
      <a *ngFor="let r of reports" [routerLink]="['/reports',r.key]" routerLinkActive="active" (click)="mobileOpen=false"><span>▸</span>{{r.label}}</a>
      <div class="sb-group">System</div>
      <a routerLink="/masters/company" routerLinkActive="active" (click)="mobileOpen=false"><span>🏛</span>Company Master</a>
      <a routerLink="/reports/backup" routerLinkActive="active" (click)="mobileOpen=false"><span>🗄</span>Backup & Restore</a>
      <a routerLink="/reports/help" routerLinkActive="active" (click)="mobileOpen=false"><span>?</span>Help & Shortcuts</a>
    </nav>
    <button class="logout" (click)="logout()">↪ Logout</button>
  </aside>
  <main class="main">
    <header class="topbar"><button class="menu" (click)="mobileOpen=!mobileOpen">☰</button><div><div class="tb-title">TubaSmart Office</div><div class="tb-sub">Office Leasing & Billing</div></div><div class="tb-right"><span class="user">{{username}} <b>{{role}}</b></span><button class="btn btn-p" routerLink="/billing">＋ New Billing</button></div></header>
    <section class="content"><router-outlet></router-outlet></section>
  </main>
</div>`})
export class ShellComponent{
 mobileOpen=false; username=localStorage.getItem('username')||'Admin'; role=localStorage.getItem('role')||'ADMIN';
 propertyMasters=[{key:'buildings',label:'Building Master',icon:'🏢'},{key:'floors',label:'Floor Master',icon:'▤'},{key:'floor-prices',label:'Floor Price Master',icon:'₹'},{key:'allotments',label:'Tenant Allotment',icon:'🔑'},{key:'charge-schedules',label:'Charge Schedules',icon:'🗓'}];
 billingMasters=[{key:'customers',label:'Customer Master',icon:'👥'},{key:'charge-types',label:'Charge / Service Type',icon:'≡'},{key:'invoice-types',label:'Invoice Type Master',icon:'📑'},{key:'sac-hsn',label:'SAC / HSN & GST',icon:'%'}];
 reports=[{key:'invoice-register',label:'Invoice Register'},{key:'credit-notes',label:'Credit Note Register'},{key:'gst',label:'GST Summary (GSTR-1)'},{key:'sac',label:'SAC / HSN Summary'},{key:'ledger',label:'Customer Ledger'},{key:'outstanding',label:'Outstanding & Ageing'},{key:'receipts',label:'Receipts Register'},{key:'occupancy',label:'Occupancy & Rent Roll'},{key:'revenue',label:'Revenue Summary'}];
 constructor(private auth:AuthService,private router:Router){} logout(){this.auth.logout();this.router.navigate(['/login']);}
}
