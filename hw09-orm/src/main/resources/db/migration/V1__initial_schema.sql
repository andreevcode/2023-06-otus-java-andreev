create table if not exists test(
    id   int,
    name varchar(50)
);
create table if not exists client(
    id   bigserial not null primary key,
    name varchar(50)
);

create table if not exists manager(
    no bigserial not null primary key,
    label text,
    param1 text
);

