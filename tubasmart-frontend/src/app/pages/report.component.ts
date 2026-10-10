import {Component,inject} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {NgFor,NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {ApiService} from '../core/api.service';
const TITLES:any={
'invoice-register':'Invoice Register','credit-notes':'Credit Note Register','gst':'GST Summary (GSTR-1)','sac':'SAC / HSN Summary','ledger':'Customer Ledger','outstanding':'Outstanding & Ageing','receipts':'Receipts Register','occupancy':'Occupancy & Rent Roll','revenue':'Revenue Summary','backup':'Backup & Restore','help':'Help & Shortcuts'};
// Columns shown per report (keys come from the backend report endpoints)
const COLS:any={
'invoice-register':['documentNumber','tenant','billingPeriod','dueDate','status','subtotal','discount','cgst','sgst','igst','total','tds','netPayable'],
'receipts':['receiptNumber','tenant','receiptDate','mode','reference','status','amount','unallocatedAmount'],
'credit-notes':['documentNumber','originalInvoice','status','taxableValue','taxImpact','total','reason'],
'sac':['sacHsnCode','count','taxable','tax'],
'outstanding':['invoice','customer','dueDate','invoiceTotal','paid','creditNotes','outstanding','ageDays','ageBucket'],
'occupancy':['building','floor','area','status','tenant','agreementNumber','monthlyRent'],
'ledger':['date','document','type','debit','credit','balance']};
@Component({selector:'app-report',standalone:true,imports:[NgFor,NgIf,FormsModule],template:`
<div class="page"><div class="page-head"><div><h1>{{title}}</h1><p>{{subtitle}}</p></div><button class="btn" *ngIf="rows.length" (click)="exportCsv()">⬇ CSV</button></div>
<div class="note note-warn" *ngIf="error">{{error}}</div>
<ng-container *ngIf="type==='backup'"><div class="grid g2"><div class="card"><div class="card-h"><h3>Backup</h3></div><div class="card-b"><p>Downloads a JSON export of customers, floors, prices, allotments, invoices, receipts and credit notes (ADMIN only). For production recovery, also back up the PostgreSQL database itself.</p><button class="btn btn-p" (click)="downloadJson()">⬇ Download data export (JSON)</button></div></div><div class="card"><div class="card-h"><h3>Record counts</h3></div><div class="card-b"><div class="kv" *ngFor="let x of backupCounts"><span>{{x.name}}</span><b>{{x.count}}</b></div></div></div></div></ng-container>
<ng-container *ngIf="type==='help'"><div class="grid g2"><div class="card"><div class="card-h"><h3>Recommended setup order</h3></div><div class="card-b"><ol class="help-list"><li>Company Master (set the State Code and invoice prefix)</li><li>SAC / HSN & GST (codes with GST rates)</li><li>Charge / Service Types (link a SAC code, mark recurring)</li><li>Invoice Types</li><li>Buildings → Floors → Floor Prices</li><li>Customers</li><li>Tenant Allotments (and optional charge schedules)</li><li>Monthly Billing Run</li><li>Receipts, allocation and reports</li></ol></div></div><div class="card"><div class="card-h"><h3>GST workflow</h3></div><div class="card-b"><p>The GST rate comes from the SAC/HSN code linked to each charge type. The backend splits it into CGST + SGST when the place of supply matches the company state, otherwise IGST.</p><p>Use the invoice and receipt registers, ledger and outstanding report to reconcile collections against billed amounts.</p></div></div></div></ng-container>
<ng-container *ngIf="type!=='backup' && type!=='help'"><div class="card"><div class="toolbar"><div class="search grow"><input class="inp" [(ngModel)]="search" placeholder="Search report…"></div><select *ngIf="type==='ledger'" class="inp" style="width:auto" [(ngModel)]="customerId" (ngModelChange)="configure()"><option [ngValue]="null">Select customer</option><option *ngFor="let c of customers" [ngValue]="c.id">{{c.code}} — {{c.name}}</option></select><button class="btn" (click)="configure()">↻ Refresh</button></div><div class="tw"><table class="tbl"><thead><tr><th *ngFor="let c of columns">{{label(c)}}</th></tr></thead><tbody><tr *ngFor="let r of filtered()"><td *ngFor="let c of columns">{{value(r,c)}}</td></tr><tr *ngIf="!rows.length"><td [attr.colspan]="columns.length||1" class="tbl-empty">{{type==='ledger'&&!customerId?'Select a customer.':'No data found.'}}</td></tr></tbody></table></div></div></ng-container></div>`})
export class ReportComponent{
 type='';title='';subtitle='';rows:any[]=[];columns:string[]=[];search='';error='';customers:any[]=[];customerId:number|null=null;backupCounts:any[]=[];
 private api=inject(ApiService);private route=inject(ActivatedRoute);
 constructor(){this.route.params.subscribe(p=>{this.type=p['type'];this.title=TITLES[this.type]||'Report';this.subtitle='Live data from the backend.';this.rows=[];this.columns=COLS[this.type]||[];this.error='';this.customerId=null;if(this.type==='ledger')this.api.get<any[]>('/masters/customers').subscribe(x=>this.customers=x||[]);this.configure()})}
 configure(){this.error='';
  if(this.type==='backup'){this.loadCounts();return}
  if(this.type==='help')return;
  if(this.type==='ledger'){if(!this.customerId){this.rows=[];return}this.fetch('/reports/ledger/'+this.customerId,true);return}
  this.fetch('/reports/'+this.type);}
 private fetch(path:string,ledger=false){this.api.get<any>(path).subscribe({next:x=>{
   if(Array.isArray(x)){this.rows=ledger?this.withBalance(x):x}
   else if(x&&typeof x==='object'){this.columns=['metric','value'];this.rows=Object.keys(x).map(k=>({metric:k,value:x[k]}))}
   else this.rows=[]},error:e=>{this.rows=[];this.error=e?.error?.message||'Could not load this report'}})}
 withBalance(x:any[]){let b=0;return x.map(r=>{b+=Number(r.debit||0)-Number(r.credit||0);return {...r,balance:Math.round(b*100)/100}})}
 label(c:string){return c.replace(/([A-Z])/g,' $1').replace(/^./,m=>m.toUpperCase())}
 filtered(){const q=this.search.toLowerCase();return this.rows.filter(r=>!q||JSON.stringify(r).toLowerCase().includes(q))}
 value(r:any,c:string){const v=r?.[c];if(v===null||v===undefined)return '—';if(typeof v==='object')return v.name??v.documentNumber??v.code??v.id??JSON.stringify(v);if(typeof v==='number'&&!Number.isInteger(v))return v.toFixed(2);return v}
 exportCsv(){const q=(x:any)=>'"'+String(x).replace(/"/g,'""')+'"';const csv=[this.columns.map(q).join(','),...this.filtered().map(r=>this.columns.map(c=>q(this.value(r,c))).join(','))].join('\n');const a=document.createElement('a');a.href=URL.createObjectURL(new Blob([csv],{type:'text/csv'}));a.download=this.type+'.csv';a.click();URL.revokeObjectURL(a.href)}
 loadCounts(){this.api.get<any>('/reports/backup').subscribe({next:b=>this.backupCounts=['customers','floors','floorPrices','allotments','invoices','receipts','creditNotes'].map(k=>({name:k,count:(b?.[k]||[]).length})),error:e=>this.error=e?.status===403?'Backup is restricted to ADMIN users.':(e?.error?.message||'Could not load backup data')})}
 downloadJson(){this.api.get<any>('/reports/backup').subscribe({next:b=>{const a=document.createElement('a');a.href=URL.createObjectURL(new Blob([JSON.stringify(b,null,2)],{type:'application/json'}));a.download='tubasmart-backup-'+new Date().toISOString().slice(0,10)+'.json';a.click();URL.revokeObjectURL(a.href)},error:e=>this.error=e?.status===403?'Backup is restricted to ADMIN users.':(e?.error?.message||'Backup download failed')})}
}
