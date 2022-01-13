create table users (
  id bigint auto_increment primary key not null,
  username varchar(100) not null,
  hashedPassword varchar(200) not null,
  roles varchar(400) not null
);
create unique index on users(username);
