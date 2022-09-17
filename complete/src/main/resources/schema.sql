DROP TABLE IF EXISTS USER_TABLE;

create table if not exists USER_TABLE (
USER_ID int not null primary key auto_increment,
USER_NAME varchar(100),
USER_SEX varchar(1),
USER_AGE NUMERIC(3));