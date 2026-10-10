import {Routes} from '@angular/router';
import {LoginComponent} from './pages/login.component';
import {DashboardComponent} from './pages/dashboard.component';
import {MasterComponent} from './pages/master.component';
import {InvoiceComponent} from './pages/invoice.component';
import {ReceiptComponent} from './pages/receipt.component';
import {BillingComponent} from './pages/billing.component';
import {ReportComponent} from './pages/report.component';
import {authGuard} from './core/auth.guard';
export const routes:Routes=[
 {path:'login',component:LoginComponent},
 {path:'',canActivate:[authGuard],component:DashboardComponent},
 {path:'dashboard',canActivate:[authGuard],component:DashboardComponent},
 {path:'masters/:type',canActivate:[authGuard],component:MasterComponent},
 {path:'invoices',canActivate:[authGuard],component:InvoiceComponent},
 {path:'receipts',canActivate:[authGuard],component:ReceiptComponent},
 {path:'billing',canActivate:[authGuard],component:BillingComponent},
 {path:'reports/:type',canActivate:[authGuard],component:ReportComponent},
 {path:'**',redirectTo:''}
];
