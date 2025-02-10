create table App_User
(
    id       INT primary key auto_increment,
    username varchar(32)
);

create table Todo
(
    id        INT primary key auto_increment,
    title     varchar(32),
    completed boolean,
    user_id   int,
    foreign key (user_id) references APP_USER (id)
);
