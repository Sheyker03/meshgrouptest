create table if not exists phoneData
(
    id     bigserial   not null primary key,
    userId bigint      not null references "users" (id),
    phone  varchar(13) not null unique
);