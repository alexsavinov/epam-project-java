drop table if exists routes, stations, orders, users;

######################################
# USERS

drop table if exists users;

create table users
(
    id       int auto_increment
        primary key,
    name     VARCHAR(50)  null,
    password VARCHAR(255) null,
    isadmin  boolean      not null default 0

);


insert into users (name, password, isadmin)
values ('admin', SHA2('admin', 0), 1);
insert into users (name, password, isadmin)
values ('user1', SHA2('pass1', 0), 0);
insert into users (name, password, isadmin)
values ('user2', SHA2('pass2', 0), 0);
insert into users (name, password, isadmin)
values ('user3', SHA2('pass2', 0), 0);

######################################
# STATIONS


CREATE TABLE stations
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(45) NULL,
    PRIMARY KEY (id)
);

# SELECT id, name FROM stations;

# INSERT INTO stations (id, name) VALUES (?, ?);

# UPDATE stations SET id = ?, name = ? WHERE id = ?;

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

# SELECT id,
#        station_departure_id,
#        station_arrival_id,
#        train_number,
#        date_departure,
#        date_arrival,
#        travel_time,
#        travel_cost,
#        seats_reserved,
#        seats_total
# FROM routes;

# INSERT INTO routes
# (id,
#  station_departure_id,
#  station_arrival_id,
#  train_number,
#  date_departure,
#  date_arrival,
#  travel_time,
#  travel_cost,
#  seats_reserved,
#  seats_total)
# VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);


# UPDATE routes
# SET id = ?, station_departure_id = ?, station_arrival_id = ?,
#     train_number = ?, date_departure = ?, date_arrival = ?,
#     travel_time = ?, travel_cost = ?, seats_reserved = ?, seats_total = ?
# WHERE id = ?;

INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (1, 2, 'A01', '2022-09-23 12:45:56', '2022-09-24 13:45:56', 50, 13, 100);

INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (2, 4, 'A02', '2022-09-11 12:45:56', '2022-09-12 20:45:00', 35, 1, 150);

INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (3, 5, 'A03', '2022-09-23 12:45:53', '2022-09-24 13:45:53', 60, 0, 80);

INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (6, 1, 'A04', '2022-09-05 12:45:54', '2022-09-06 13:45:54', 34, 14, 150);

INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (5, 4, 'A05', '2022-10-05 12:45:54', '2022-10-16 13:45:00', 22, 0, 95);


INSERT INTO routes
(station_departure_id,
 station_arrival_id,
 train_number,
 date_departure,
 date_arrival,
 travel_cost,
 seats_reserved,
 seats_total)
VALUES (5, 3, 'A06', '2022-10-20 01:45:54', '2022-10-20 13:45:00', 50, 0, 200);



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



# SELECT user_id,
#        route_id,
#        seats
# FROM orders;

# INSERT INTO orders (user_id, route_id, seats)
# VALUES (?, ?, ?);

# UPDATE orders
# SET user_id  = ?,
#     route_id = ?,
#     seats    = ?
# WHERE user_id = ?
#   AND route_id = ?;

INSERT INTO orders (user_id, route_id, seats, date_reserve)
VALUES (1, 1, 11, '2022-09-23 12:45:54');

INSERT INTO orders (user_id, route_id, seats, date_reserve)
VALUES (3, 1, 2, '2022-09-24 12:45:54');

INSERT INTO orders (user_id, route_id, seats, date_reserve)
VALUES (2, 2, 1, '2022-09-25 12:45:54');

INSERT INTO orders (user_id, route_id, seats, date_reserve)
VALUES (2, 4, 4, '2022-09-27 12:45:54');

INSERT INTO orders (user_id, route_id, seats, date_reserve)
VALUES (3, 4, 10, '2022-09-27 12:45:54');