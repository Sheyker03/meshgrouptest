create domain account_balance_domain as numeric(19, 2) check (
    VALUE >= 0
    );