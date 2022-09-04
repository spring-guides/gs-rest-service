DROP TABLE IF EXISTS USER;

create table if not exists USER (
USER_ID int not null primary key auto_increment,
USER_NAME varchar(100),
USER_SEX varchar(1),
USER_AGE NUMERIC(3));