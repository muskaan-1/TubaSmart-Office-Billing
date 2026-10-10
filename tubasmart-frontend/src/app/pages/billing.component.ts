import {Component,inject} from '@angular/core';
import {NgFor,NgIf,DecimalPipe} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {ApiService} from '../core/api.service';
@Component({selector:'app-billing',standalone:true,imports:[NgFor,NgIf,DecimalPipe,FormsModule,RouterLink],template:`
<div class="page"><div class="page-head"><div><h1>Monthly Billing Run</h1><p>Bills a tenant from their allotment, floor prices and charge schedules. GST, TDS and numbering are calculated by the backend.</p></div></div>
<div class="card"><div class="card-h"><div><h3>Run parameters</h3><div class="sub">Pick the tenant and month, preview, then generate.</div></div></div><div class="card-b">
<div class="grid g4">
<div class="field"><label>Tenant <span class="req">*</span></label><select class="inp" [(ngModel)]="form.tenantId" (ngModelChange)="clear()"><option [ngValue]="null">Select tenant</option><option *ngFor="let x of customers" [ngValue]="x.id">{{x.code}} — {{x.name}}</option></select></div>
<div class="field"><label>Billing Month <span class="req">*</span></label><input class="inp" type="month" [(ngModel)]="form.billingPeriod" (ngModelChange)="clear()"></div>
<div class="field"><label>Place of supply (optional)</label><input class="inp" [(ngModel)]="form.supplyState" placeholder="Defaults to tenant's state"></div>
<div class="field"><label>Company state (optional)</label><input class="inp" [(ngModel)]="form.companyState" placeholder="Defaults to Company Master state code"></div>
</div>
<div class="note note-info" style="margin-top:10px"><b>CGST+SGST vs IGST</b><span>The place of supply is compared with the company state. Use the same spelling/format in both (e.g. both "Karnataka" or both "29"), otherwise it is treated as inter-state (IGST).</span></div>
<div class="actions"><button class="btn" (click)="preview()" [disabled]="loading||!valid()">⟳ Preview</button><button class="btn btn-p" (click)="generate()" [disabled]="loading||!valid()||!p">{{loading?'Working…':'✔ Generate Invoice'}}</button></div>
<div class="note note-warn" *ngIf="error" style="margin-top:10px">{{error}}</div>
</div></div>
<div class="card" *ngIf="p"><div class="card-h"><h3>Preview — {{p.billingPeriod}}</h3></div><div class="card-b"><div class="tw"><table class="tbl"><thead><tr><th>Charge</th><th>Description</th><th>SAC/HSN</th><th class="r">Amount</th><th class="r">Discount</th><th class="r">Taxable</th><th class="r">GST %</th><th class="r">GST</th></tr></thead><tbody><tr *ngFor="let l of p.lines"><td>{{l.chargeType}}</td><td>{{l.description}}</td><td class="mono">{{l.sacHsnCode||'—'}}</td><td class="r">{{l.amount|number:'1.2-2'}}</td><td class="r">{{l.discount|number:'1.2-2'}}</td><td class="r">{{l.taxable|number:'1.2-2'}}</td><td class="r">{{l.taxRate|number:'1.0-2'}}</td><td class="r">{{l.taxAmount|number:'1.2-2'}}</td></tr></tbody></table></div>
<div class="grid g3" style="margin-top:12px"><div><div class="kv"><span>Subtotal</span><b>₹ {{p.subtotal|number:'1.2-2'}}</b></div><div class="kv"><span>Discount</span><b>₹ {{p.discount|number:'1.2-2'}}</b></div><div class="kv"><span>Taxable</span><b>₹ {{p.taxable|number:'1.2-2'}}</b></div></div><div><div class="kv"><span>CGST</span><b>₹ {{p.cgst|number:'1.2-2'}}</b></div><div class="kv"><span>SGST</span><b>₹ {{p.sgst|number:'1.2-2'}}</b></div><div class="kv"><span>IGST</span><b>₹ {{p.igst|number:'1.2-2'}}</b></div></div><div><div class="kv"><span>Invoice total</span><b>₹ {{p.total|number:'1.2-2'}}</b></div><div class="kv"><span>TDS</span><b>₹ {{p.tds|number:'1.2-2'}}</b></div><div class="kv"><span>Net payable</span><b>₹ {{p.netPayable|number:'1.2-2'}}</b></div></div></div></div></div>
<div class="note note-info" *ngIf="invoice"><b>Invoice generated</b><span>{{invoice.documentNumber}} — total ₹ {{invoice.total|number:'1.2-2'}}. <a routerLink="/invoices">Open invoice register</a></span></div>
</div>`})
export class BillingComponent{
 customers:any[]=[];p:any=null;invoice:any=null;loading=false;error='';
 form:any={tenantId:null,billingPeriod:new Date().toISOString().slice(0,7),supplyState:'',companyState:''};
 private api=inject(ApiService);
 constructor(){this.api.get<any[]>('/masters/customers').subscribe(x=>this.customers=(x||[]).filter(c=>c.active!==false))}
 valid(){return !!this.form.tenantId&&!!this.form.billingPeriod}
 clear(){this.p=null;this.invoice=null;this.error=''}
 private body(){return {tenantId:Number(this.form.tenantId),billingPeriod:this.form.billingPeriod,supplyState:this.form.supplyState||null,companyState:this.form.companyState||null}}
 preview(){this.clear();this.loading=true;this.api.post<any>('/billing/runs/preview',this.body()).subscribe({next:x=>{this.p=x;this.loading=false},error:e=>{this.error=e?.error?.message||'Preview failed';this.loading=false}})}
 generate(){this.loading=true;this.error='';this.api.post<any>('/billing/runs/generate-from-prices',this.body()).subscribe({next:x=>{this.invoice=x;this.p=null;this.loading=false},error:e=>{this.error=e?.error?.message||'Billing generation failed';this.loading=false}})}
}
