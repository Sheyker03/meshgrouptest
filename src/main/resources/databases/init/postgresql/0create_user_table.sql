create table if not exists "users"
(
    id          bigserial    not null primary key,
    name        varchar(500) not null,
    dateOfBirth date         not null,
    password    varchar(500) not null check ( length(password) >= 8 is not null)
);