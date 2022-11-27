use railwaydb;
drop table if exists routes, stations, orders, users;


######################################
# USERS

drop table if exists users;

create table users
(
    id               int auto_increment
        primary key,
    name             VARCHAR(50)  null,
    password         VARCHAR(255) null,
    email            VARCHAR(255) null,
    isadmin          boolean      not null default 0,
    isactive         boolean      not null default 0,
    activation_token VARCHAR(255) null

);

insert into users (name, password, email, isadmin, isactive)
values ('admin', SHA2('admin', 0), 'spell478@gmail.com', 1, 1);
insert into users (name, password, email, isadmin, isactive)
values ('user1', SHA2('user1', 0), 'user1@railway.itermit.com', 0, 1);
insert into users (name, password, email, isadmin, isactive)
values ('user2', SHA2('user2', 0), 'user2@railway.itermit.com', 0, 1);
insert into users (name, password, email, isadmin, isactive)
values ('user3', SHA2('user3', 0), 'user3@railway.itermit.com', 0, 1);
insert into users (name, password, email, isadmin, isactive)
values ('user4', SHA2('user4', 0), 'user4@railway.itermit.com', 0, 1);
insert into users (name, password, email, isadmin, isactive)
values ('user5', SHA2('user5', 0), 'user5@railway.itermit.com', 0, 1);
insert into users (name, password, email, isadmin, isactive)
values ('user6', SHA2('user6', 0), 'user6@railway.itermit.com', 0, 0);
insert into users (name, password, email, isadmin, isactive)
values ('user7', SHA2('user7', 0), 'user7@railway.itermit.com', 0, 1);
insert into users (name, password, email, isadmin, isactive)
values ('user8', SHA2('user8', 0), 'user8@railway.itermit.com', 0, 0);
insert into users (name, password, email, isadmin, isactive)
values ('user9', SHA2('user9', 0), 'user9@railway.itermit.com', 0, 0);


######################################
# STATIONS

CREATE TABLE stations
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(45) NULL,
    PRIMARY KEY (id)
);

INSERT INTO stations (name)
VALUES ('Stockton');

INSERT INTO stations (name)
VALUES ('Lathrop');

INSERT INTO stations (name)
VALUES ('Tracy');

INSERT INTO stations (name)
VALUES ('Livermore');

INSERT INTO stations (name)
VALUES ('Pleasanton');

INSERT INTO stations (name)
VALUES ('Fremont');

INSERT INTO stations (name)
VALUES ('Santa Clara');

INSERT INTO stations (name)
VALUES ('San Jose');


######################################
# ROUTES

CREATE TABLE routes
(
    id                   INT         NOT NULL AUTO_INCREMENT,
    station_departure_id INT         NULL,
    station_arrival_id   INT         NULL,
    train_number         VARCHAR(45) NULL,
    date_departure       DATETIME    NULL,
    date_arrival         DATETIME    NULL,
    travel_cost          INT         NULL,
    seats_reserved       INT         NULL,
    seats_total          INT         NULL,
    PRIMARY KEY (id),
    INDEX station_departure_id_idx (station_departure_id ASC) VISIBLE,
    INDEX station_arrival_id_idx (station_arrival_id ASC) VISIBLE,
    CONSTRAINT station_departure_id
        FOREIGN KEY (station_departure_id)
            REFERENCES stations (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT station_arrival_id
        FOREIGN KEY (station_arrival_id)
            REFERENCES stations (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (1, 2, 'A01', '2022-11-23 12:45:56', '2022-11-24 13:45:56', 50, 13, 100);

INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (2, 4, 'A02', '2022-11-11 12:45:56', '2022-11-12 20:45:00', 35, 1, 150);

INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (3, 5, 'A03', '2022-11-23 12:45:53', '2022-11-24 13:45:53', 60, 0, 80);

INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (6, 1, 'A04', '2022-11-05 12:45:54', '2022-11-06 13:45:54', 34, 14, 150);

INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (5, 4, 'A05', '2022-12-05 12:45:54', '2022-12-16 13:45:00', 22, 0, 95);


INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (5, 3, 'A06', '2022-12-20 01:45:54', '2022-12-20 13:45:00', 50, 0, 200);


######################################
# orders

CREATE TABLE orders
(
    id           int unsigned NOT NULL AUTO_INCREMENT KEY,
    user_id      int          NOT NULL,
    route_id     int          NOT NULL,
    seats        int DEFAULT NULL,
    date_reserve DATETIME     NULL,
#     UNIQUE KEY user_route_id (route_id, user_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (route_id) REFERENCES routes (id)
);

INSERT INTO orders (user_id, route_id, seats, date_reserve)
VALUES (1, 1, 11, '2022-11-23 12:45:54');

INSERT INTO orders (user_id, route_id, seats, date_reserve)
VALUES (3, 1, 2, '2022-11-24 12:45:54');

INSERT INTO orders (user_id, route_id, seats, date_reserve)
VALUES (2, 2, 1, '2022-11-25 12:45:54');

INSERT INTO orders (user_id, route_id, seats, date_reserve)
VALUES (2, 4, 4, '2022-11-27 12:45:54');

INSERT INTO orders (user_id, route_id, seats, date_reserve)
VALUES (3, 4, 10, '2022-11-27 12:45:54');
