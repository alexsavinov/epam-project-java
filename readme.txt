Railway ticket office
=====================

The task of the final project is to develop a web application
that supports the functionality according to the task variant.

The administrator can add / delete / edit the list of Stations and Railway Routes between them.

The route contains information as:
- starting station and departure time;
- final station and time of arrival.

The user can search for routes between stations that interest him.

The result of the search is a list of trains, each string of which contains:
- train number;
- time / date and station of departure;
- travel time;
- time / date and station of arrival;
- number of available seats;
- travel cost;
- link to the corresponding route view page (user can view route information).

If the user is registered in the system, he must be able to buy a ticket
for the selected route on the specified date (subject to availability).


INSTALL
=====================

# STEP 1
copy file:
    src/main/webapp/WEB-INF/classes/app.properties.example
to:
    src/main/webapp/WEB-INF/classes/app.properties
and edit.

# STEP 2
copy file:
    src/main/webapp/META-INF/context.xml.example
to:
    src/main/webapp/META-INF/context.xml
and edit.

# STEP 3
Run following commands in mysql console:
    create database __DBNAME__;

    CREATE USER '__USER__'@'__HOST__' IDENTIFIED BY '__PASSWORD__';
    GRANT ALL PRIVILEGES ON __DBNAME__ . * TO '__USER__'@'__HOST__';
    FLUSH PRIVILEGES;

    systemctl restart mysql
Replace __NAMES__ with you values.

# STEP 4
Run script in mysql console:
    mysql-run.sql
It will create db Schema and fill with test Data.

