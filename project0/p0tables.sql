create user guest identified by "guestisverysecure";
grant create session to guest;

CREATE PUBLIC SYNONYM p0_checking FOR root.p0_checking;
CREATE PUBLIC SYNONYM p0_users FOR root.p0_users;

create public synonym createuser for root.createuser;
create public synonym createcheckingaccount for root.createcheckingaccount;
create public synonym withdrawmoney for root.withdrawmoney;
create public synonym depositmoney for root.depositmoney;
create public synonym transfermoney for root.transfermoney;
create public synonym deletecheckingaccount for root.deletecheckingaccount;
create public synonym deleteuse for root.deleteuser;

GRANT
  SELECT,
  INSERT,
  UPDATE,
  DELETE
ON
  p0_users
TO
  guest;


GRANT
  SELECT,
  INSERT,
  UPDATE,
  DELETE
ON
  p0_checking
TO
  guest;
  
grant execute on createuser to guest;
grant execute on createcheckingaccount to guest;
grant execute on withdrawmoney to guest;
grant execute on depositmoney to guest;
grant execute on transfermoney to guest;
grant execute on deletecheckingaccount to guest;
grant execute on deleteuser to guest;

GRANT UNLIMITED TABLESPACE TO guest;

create sequence p0_userid_seq
start with 1
increment by 1
minvalue 0
;
create sequence p0_bankid_checking_seq
start with 1
increment by 1
minvalue 1
;
create sequence p0_bankid_savings_seq
start with 1
increment by 1
minvalue 1
;

create table p0_users (
    USER_ID number default p0_userid_seq.nextval,
    USERNAME varchar2(20) not null unique,
    PASSCODE varchar2(20) not null,
    ADMINACCESS number(1) not null,
    PRIMARY KEY(USER_ID) 
);

create table p0_checking (
    USER_ID number not null,
    BANK_ACCOUNT_ID number default p0_bankid_checking_seq.nextval,
    BALANCE number(38,2) not null,
    PRIMARY KEY(BANK_ACCOUNT_ID),
    CONSTRAINT USER_ID FOREIGN KEY (USER_ID) REFERENCES p0_users(USER_ID) ON DELETE CASCADE
);

create or replace procedure createuser(username in varchar2, passcode in varchar2, adminaccess in number, userid out number)
is
begin
    declare usrnm varchar2(20);
    begin
        usrnm := LOWER(username);
        insert into p0_users(username, passcode, adminaccess)
        values(usrnm, passcode, adminaccess);
        userid := p0_userid_seq.currval;
    end;
end createuser;
/

create or replace procedure createcheckingaccount(userid in number, bankaccountid out number)
is
begin
    insert into p0_checking(user_id, bank_account_id, balance)
    values(userid, p0_bankid_checking_seq.nextval, 0.0);
    bankaccountid := p0_bankid_checking_seq.currval;
end createcheckingaccount;
/

create or replace function subtractmoney(amount1 in number, amount2 in number)
return number 
is newamount number(38, 2);
begin
    newamount := amount1 - amount2;
    return(newamount);
end subtractmoney;
/

create or replace function addmoney(amount1 in number, amount2 in number)
return number 
is newamount number(38, 2);
begin
    newamount := amount1 + amount2;
    return(newamount);
end addmoney;
/


create or replace procedure withdrawmoney( amount in number, bankid in number, userid in number, newamount out number, p0_recordset out sys_refcursor)
as
begin 
    declare mybalance number(38,2);
    begin
        if amount > 0 then
            select balance into mybalance from p0_checking where bank_account_id = bankid;
            declare newbalance number(38,2);
            begin
                if mybalance >= amount then
                    newbalance:= subtractmoney(mybalance, amount);
                    update p0_checking
                    set balance = newbalance where bank_account_id = bankid;
                    newamount:= newbalance;
                else
                    newamount:= -1;
                end if;
            end;
        end if;
        begin 
            if userid = -1 then
                open p0_recordset for
                    select * from p0_checking;
            else
                open p0_recordset for
                    select * from p0_checking where user_id = userid;
            end if;
        end;
    end;
end withdrawmoney;
/

create or replace procedure depositmoney(amount in number, bankid in number, userid in number, newamount out number, p0_recordset out sys_refcursor)
as 
    begin
        declare mybalance number(38,2);
        begin
            if amount > 0 then
                select balance into mybalance from p0_checking where bank_account_id = bankid;
                declare newbalance number(38,2);
                begin
                    newbalance:= addmoney(mybalance, amount);
                    update p0_checking 
                    set balance = newbalance where bank_account_id = bankid;
                    newamount := newbalance;
                end;
            end if;
        begin 
            if userid = -1 then
                open p0_recordset for
                    select * from p0_checking;
            else
                open p0_recordset for
                    select * from p0_checking where user_id = userid;
            end if;
        end;
    end;
end depositmoney;
/

create or replace procedure transfermoney(amount in number, bankidsource in number, 
                                            bankidtarget in number, userid in number, 
                                            newamount out number, p0_recordset out sys_refcursor)
as
    begin
        declare mybalance number(38, 2);
        begin
        select balance into mybalance from p0_checking where bank_account_id = bankidsource;
        declare newbalance number(38,2);
            begin
                if mybalance >= amount and amount > 0 then
                    newbalance:= subtractmoney(mybalance, amount);
                    update p0_checking
                    set balance = newbalance where bank_account_id = bankidsource;
                    newamount:= newbalance;
                else
                    newamount:= -1;
                end if;
            end;
            declare targetbalance number(38,2);
            begin
                select balance into targetbalance from p0_checking where bank_account_id = bankidtarget;
                declare newbalancetarget number(38,2);
                begin
                    if not newamount = -1 then
                        newbalancetarget:= addmoney(targetbalance, amount);
                        update p0_checking 
                        set balance = newbalancetarget where bank_account_id = bankidtarget;
                    end if;
                end;
            end;
        begin 
            if userid = -1 then
                open p0_recordset for
                    select * from p0_checking;
            else 
                open p0_recordset for
                    select * from p0_checking where user_id = userid;
            end if;
        end;
    end;
            
end transfermoney;
/

create or replace procedure deletecheckingaccount(bankid in number, invoker in number, succ out number, p0_recordset out sys_refcursor)
as 
begin
        declare 
            mybalance number(38,2);
            status number(1);
            bankid_userid number;
        begin 
            select balance into mybalance from p0_checking where bank_account_id = bankid;
            if mybalance = 0 then
                select adminaccess into status from p0_users where user_id = invoker;
                select user_id into bankid_userid from p0_checking where bank_account_id = bankid;
                if status = 1 or bankid_userid = invoker then
                    delete from p0_checking where bank_account_id = bankid;
                    succ := 1;
                else
                    succ := 0;
                end if;
            else 
                succ:= 0;
            end if;
        end;
        begin 
            open p0_recordset for
                select * from p0_checking where user_id = invoker;
        end;
end deletecheckingaccount;
/

-- procedure will fail unless user was granted access to it...
create or replace procedure deletecheckingaccountasadmin(bankid in number, succ out number, p0_recordset out sys_refcursor)
as 
begin
        declare 
            bankid_userid number;
        begin
            delete from p0_checking where bank_account_id = bankid;
            succ := 1;
        end;
        begin 
            open p0_recordset for
                select * from p0_checking;
        end;
end deletecheckingaccountasadmin;
/

create or replace procedure deleteuser(userid in number, succ out number)
as
begin
    declare balance_exists number; 
    begin
        select count(balance) into balance_exists from p0_checking where user_id = userid and balance > 0;
        if balance_exists = 0 then
            -- we can delete
            delete from p0_checking where user_id = userid;
            delete from p0_users where user_id = userid;
            succ := 1;
        else 
            succ := 0;
        end if;
    end;
end deleteuser;
/

create or replace procedure deleteuserasadmin(userid in number, p0_recordset out sys_refcursor)
as
begin
    delete from p0_users where user_id = userid;
    open p0_recordset for
        select * from p0_users;
end deleteuserasadmin;
/

declare 
    out1 number;
    out2 sys_refcursor;
begin
deletecheckingaccount(61, 41, out1, out2);
end;