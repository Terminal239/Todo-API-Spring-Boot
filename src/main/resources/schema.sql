create table Todo
(
    id        INT primary key auto_increment,
    title     varchar(32),
    completed boolean
);

create table App_User
(
    id    INT primary key auto_increment,
    username varchar(32)
)