SET SCHEMA PUBLIC;

create table if not exists Taco_Order
(
    id              identity    not null primary key,
    delivery_name   varchar(50) not null,
    delivery_street varchar(50) not null,
    delivery_city   varchar(50) not null,
    delivery_state  varchar(2)  not null,
    delivery_zip    varchar(10) not null,
    cc_number       varchar(16) not null,
    cc_expiration   varchar(5)  not null,
    cc_cvv          varchar(3)  not null,
    taco_ids        array

);

create table if not exists Taco
(
    id             identity    not null primary key,
    name           varchar(50) not null,
    ingredient_ids array
);

create table if not exists Ingredient
(
    id   identity    not null primary key,
    slug varchar(4)  not null,
    name varchar(25) not null,
    type varchar(10) not null
);

create table if not exists User
(
    id           identity     not null primary key,
    username     varchar(50)  not null,
    password     varchar(100) not null,
    fullname     varchar(50)  not null,
    street       varchar(50)  not null,
    city         varchar(25)  not null,
    state        varchar(25)  not null,
    zip          varchar(10)  not null,
    phone_number varchar(15)  not null
);