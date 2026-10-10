-- B2: safe starter master data. Users are provisioned through the secured admin API.
insert into invoice_types(name,prefix,financial_year_series,terms,credit_days,nature,active,created_at,updated_at)
select 'Tax Invoice','INV','YYYY-YY','Payment due as per agreed credit terms',30,'SALE',true,now(),now()
where not exists (select 1 from invoice_types where lower(name)=lower('Tax Invoice'));

insert into invoice_types(name,prefix,financial_year_series,terms,credit_days,nature,active,created_at,updated_at)
select 'Proforma Invoice','PI','YYYY-YY','Proforma demand only; does not affect ledger/GST',30,'PROFORMA',true,now(),now()
where not exists (select 1 from invoice_types where lower(name)=lower('Proforma Invoice'));

insert into invoice_types(name,prefix,financial_year_series,terms,credit_days,nature,active,created_at,updated_at)
select 'Credit Note','CN','YYYY-YY','Issued against an original invoice',0,'CREDIT',true,now(),now()
where not exists (select 1 from invoice_types where lower(name)=lower('Credit Note'));

insert into invoice_types(name,prefix,financial_year_series,terms,credit_days,nature,active,created_at,updated_at)
select 'Debit Note','DN','YYYY-YY','Issued against an original invoice',0,'DEBIT',true,now(),now()
where not exists (select 1 from invoice_types where lower(name)=lower('Debit Note'));
