create table if not exists account
(
    id      bigserial     not null primary key,
    user_id bigint unique not null references "users",
    balance decimal default 0.0
);