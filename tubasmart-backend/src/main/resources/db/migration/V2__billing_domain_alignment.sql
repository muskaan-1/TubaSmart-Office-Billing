-- B1: align the persistent model with the TubaSmart Office Billing HTML business domain.
-- Existing columns are retained for frontend/API compatibility; new fields are additive.

alter table companies add column if not exists legal_name varchar(255);
alter table companies add column if not exists address1 varchar(255);
alter table companies add column if not exists address2 varchar(255);
alter table companies add column if not exists city varchar(255);
alter table companies add column if not exists state_code varchar(50);
alter table companies add column if not exists pin varchar(30);
alter table companies add column if not exists email varchar(255);
alter table companies add column if not exists phone varchar(100);
alter table companies add column if not exists website varchar(255);
alter table companies add column if not exists bank_name varchar(255);
alter table companies add column if not exists account_no varchar(100);
alter table companies add column if not exists ifsc varchar(50);
alter table companies add column if not exists branch varchar(255);
alter table companies add column if not exists sign_name varchar(255);
alter table companies add column if not exists sign_designation varchar(255);
alter table companies add column if not exists inv_prefix varchar(50);
alter table companies add column if not exists gst_enabled boolean not null default true;
alter table companies add column if not exists default_credit_days integer not null default 30;

alter table charge_types add column if not exists code varchar(50);
alter table charge_types add column if not exists is_recurring boolean not null default true;
create unique index if not exists uk_charge_types_code on charge_types(code) where code is not null;

alter table sac_hsn_gst add column if not exists type varchar(20);
alter table sac_hsn_gst add column if not exists active boolean not null default true;
create unique index if not exists uk_sac_hsn_type_code on sac_hsn_gst(type, code) where type is not null;

alter table floors add column if not exists usage varchar(255);

alter table floor_prices add column if not exists rate_basis varchar(30);
alter table floor_prices add column if not exists remarks varchar(500);
alter table floor_prices add column if not exists active boolean not null default true;

alter table tenant_allotments add column if not exists discount_pct numeric(18,4) not null default 0;
alter table tenant_allotments add column if not exists escalation_pct numeric(18,4) not null default 0;
alter table tenant_allotments add column if not exists remarks varchar(1000);
alter table tenant_allotments add column if not exists agreement_file varchar(255);
create index if not exists idx_allotment_floor_active on tenant_allotments(floor_id, active);

alter table tenant_charge_schedule add column if not exists rate numeric(18,2) not null default 0;
alter table tenant_charge_schedule add column if not exists basis varchar(30);
alter table tenant_charge_schedule add column if not exists remarks varchar(500);
alter table tenant_charge_schedule add column if not exists auto_bill boolean not null default true;

alter table invoice_types add column if not exists nature varchar(30) not null default 'SALE';
alter table invoice_types add column if not exists active boolean not null default true;

alter table invoices add column if not exists tds_rate numeric(18,4) not null default 0;
alter table invoices add column if not exists net_payable numeric(18,2) not null default 0;
alter table invoices add column if not exists round_off numeric(18,2) not null default 0;
alter table invoices add column if not exists invoice_type_id bigint;
alter table invoices add column if not exists building_id bigint;
alter table invoices add column if not exists floor_id bigint;
alter table invoices add column if not exists allotment_id bigint;
alter table invoices add column if not exists ref_invoice_id bigint;
alter table invoices add column if not exists place_of_supply varchar(50);
alter table invoices add column if not exists reference_number varchar(255);
alter table invoices add column if not exists notes varchar(2000);
alter table invoices add column if not exists terms varchar(4000);
alter table invoices add column if not exists reverse_charge boolean not null default false;
alter table invoices add column if not exists period_from date;
alter table invoices add column if not exists period_to date;
alter table invoices add column if not exists cancelled_at date;
do $$ begin alter table invoices add constraint fk_invoice_type foreign key(invoice_type_id) references invoice_types(id); exception when duplicate_object then null; end $$;
do $$ begin alter table invoices add constraint fk_invoice_building foreign key(building_id) references buildings(id); exception when duplicate_object then null; end $$;
do $$ begin alter table invoices add constraint fk_invoice_floor foreign key(floor_id) references floors(id); exception when duplicate_object then null; end $$;
do $$ begin alter table invoices add constraint fk_invoice_allotment foreign key(allotment_id) references tenant_allotments(id); exception when duplicate_object then null; end $$;
do $$ begin alter table invoices add constraint fk_invoice_ref_invoice foreign key(ref_invoice_id) references invoices(id); exception when duplicate_object then null; end $$;
create index if not exists idx_invoice_customer_date on invoices(tenant_id, created_at);
create index if not exists idx_invoice_period on invoices(period_from, period_to);

alter table invoice_lines add column if not exists discount numeric(18,2) not null default 0;
alter table invoice_lines add column if not exists taxable numeric(18,2) not null default 0;
alter table invoice_lines add column if not exists cgst numeric(18,2) not null default 0;
alter table invoice_lines add column if not exists sgst numeric(18,2) not null default 0;
alter table invoice_lines add column if not exists igst numeric(18,2) not null default 0;
alter table invoice_lines add column if not exists cess numeric(18,2) not null default 0;
alter table invoice_lines add column if not exists charge_type_id bigint;
alter table invoice_lines add column if not exists sac_hsn_gst_id bigint;
do $$ begin alter table invoice_lines add constraint fk_invoice_line_charge_type foreign key(charge_type_id) references charge_types(id); exception when duplicate_object then null; end $$;
do $$ begin alter table invoice_lines add constraint fk_invoice_line_tax_code foreign key(sac_hsn_gst_id) references sac_hsn_gst(id); exception when duplicate_object then null; end $$;

alter table receipts add column if not exists ref_date varchar(30);
alter table receipts add column if not exists bank varchar(255);
alter table receipts add column if not exists narration varchar(1000);
alter table receipts add column if not exists tds_deducted numeric(18,2) not null default 0;
alter table receipts add column if not exists status varchar(30) not null default 'ISSUED';
create index if not exists idx_receipt_tenant_date on receipts(tenant_id, receipt_date);

alter table credit_notes add column if not exists ref_no varchar(255);
alter table credit_notes add column if not exists date date;

create table if not exists app_users (
    id bigint generated by default as identity primary key,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone,
    username varchar(100) not null unique,
    password_hash varchar(255) not null,
    role varchar(40) not null,
    active boolean not null default true
);

create table if not exists audit_logs (
    id bigint generated by default as identity primary key,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone,
    username varchar(100) not null,
    action varchar(50) not null,
    entity_type varchar(100) not null,
    entity_id bigint,
    details varchar(4000),
    occurred_at timestamp with time zone not null default now()
);
create index if not exists idx_audit_occurred_at on audit_logs(occurred_at desc);

-- Seed the roles/user table only when it is empty. Password is supplied by B6 bootstrap logic.
