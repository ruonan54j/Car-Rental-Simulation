drop table Customers cascade constraints;
drop table Rentals cascade constraints;
drop table Reservations cascade constraints;
drop table Vehicles cascade constraints;
drop table VehicleTypes cascade constraints;


create table VehicleTypes(
  vtname varchar2(20) primary key,
  features varchar2(20),
  hourlyRate number(10,2) not null,
  kiloRate number(10,2) not null,
  kiloLimitPerHour number(10,2) not null);


create table Vehicles(
  vid integer primary key,
  vlicense varchar2(10) not null,
  make varchar2(10) not null,
  model varchar2(10) not null,
  year integer not null,
  color varchar2(10) not null,
  odometer number(5,2) not null,                        
  status varchar2(10) not null,
  vtname varchar2(10) not null,
  location varchar2(10) not null,
  foreign key (vtname) references VehicleTypes(vtname));

create table Customers(
  dlicense varchar2(20) primary key,
  cellphone varchar2(20) not null,
  name varchar2(10) not null,
  address varchar2(20) not null
  );

create table Reservations(
  confNo integer primary key,
  vid integer,
  dlicense varchar(20),
  startTimestamp timestamp not null,
  endTimestamp timestamp not null,
  cardName varchar(20) not null,
  cardNo varchar(20) not null,
  expDate timestamp not null, 
  foreign key (vid) references Vehicles,
  foreign key (dlicense) references Customers(dlicense));

create table Rentals(
  rid integer primary key,
  confNo integer,
  startOdometer number(6,0) not null,
  beginTimestamp timestamp,
  returnTimestamp timestamp not null,
  endOdometer number(6,0),   
  fullTank integer,
  finalCost number(6,0),
 foreign key(confNo) references Reservations(confNo));

insert into Customers values ('1233377','1233337','fay','12 Ave NW');
insert into Customers values ('1244466','1231117','hed','123 A street NW');
insert into Customers values ('1235557','1233367','sony','12 B street NW');
insert into Customers values ('1266647','1235567','Roger','3 Ave SW');
insert into Customers values ('1221117','1777767','kristy','1 street NE');
insert into Customers values ('1233117','1234467','crystal','3 elmo street NW');
insert into Customers values ('1111167','1226467','jered','13 bear street NE');


insert into VehicleTypes values ('electric','fun',52.44,21.00, 80.00);
insert into VehicleTypes values ('van','not fun',55.44,25.00, 50.00);
insert into VehicleTypes values ('cruiser','fast',52.54,20.00, 75.00);
insert into VehicleTypes values ('truck','big',58.00,22.00, 70.00);
insert into VehicleTypes values ('hybrid','yaay',56.44,20.50, 60.00);
insert into VehicleTypes values ('typeTest','test',38.00,23.00, 73.00);
insert into VehicleTypes values ('typeTest2','loud',16.44,10.50, 65.00);


insert into Vehicles values (1,'plate1','make1','m1',12,'blue',12.23,'Available','electric','vancouver');
insert into Vehicles values (2,'plate2','make2','m2',02,'red',12.23,'Available','truck','vancouver');
insert into Vehicles values (3,'plate3','make1','m2',02,'red',12.23,'Available','truck','vancouver');
insert into Vehicles values (4,'plate4','make1','m3',12,'blue',12.23,'Available','electric','vancouver');
insert into Vehicles values (5,'plate5','make2','m5',02,'red',12.23,'Available','truck','vancouver');
insert into Vehicles values (6,'plate6','make1','m2',02,'red',12.23,'Available','truck','vancouver');
