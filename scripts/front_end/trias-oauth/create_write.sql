create database trias_cli default character set utf8 collate utf8_general_ci;
create database trias default character set utf8 collate utf8_general_ci;
use trias_cli;
source /mysql/trias_cli-init.sql;
use trias;
source /mysql/trias_server-init.sql;
use mysql;
select host, user from user;
create user trias identified by 'trias@123';
grant all privileges on trias.* to trias@'%';
grant all privileges on trias_cli.* to trias@'%';
FLUSH PRIVILEGES;
