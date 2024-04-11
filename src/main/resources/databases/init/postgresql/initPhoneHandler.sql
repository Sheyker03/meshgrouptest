create domain phone_number_domain as text check (
    VALUE ~ '^[7][9]{1}[0-9]{9}$'
    );

