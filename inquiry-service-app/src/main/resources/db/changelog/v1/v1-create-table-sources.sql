create table sources (
                         id          bigserial primary key,
                         name        varchar(100) not null,
                         created_at  timestamp with time zone not null DEFAULT NOW(),
                         updated_at  timestamp with time zone not null DEFAULT NOW()
);

