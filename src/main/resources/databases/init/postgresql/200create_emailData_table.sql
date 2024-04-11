create table if not exists emailData
(
    id     bigserial    not null primary key,
    userId bigint       not null references "users" (id),
    email  varchar(200) not null unique
);