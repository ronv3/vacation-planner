# Vacation Planner
This repository contains template applications (both backend and frontend) for "Vacation Planner" exercise.

## Prerequisites

The following items have to be installed on your machine in order to get your work done:

* Docker
* Java 21
* Node.js 20+
* Angular CLI

## Prepare database (PostgreSQL 16.1)

```
docker run --name vacation-planner-db -e POSTGRES_PASSWORD=thisispassword -p 5432:5432 -d postgres:16.1
```
This command will start PostgreSQL container listening for connection on **localhost:5432**.
Username is **postgres** and password is **thisispassword**.

If for some reason you have to re-initialize Postgres container from the scratch then run:
`docker rm -fv travel-log-postgres` and execute `docker run` command again.


## Running the backend application

Under `vacation-service` folder execute the following command:
```
./gradlew run
```
After that navigate to `http://localhost:8080/vacation-requests` in your browser to verify that backend application is running and JSON response is returned.

## Running the frontend application

Navigate to `vacation-web` folder and before the first launch install required dependencies:
```
npm install
```

When dependencies are installed run the frontend application:
```
ng serve
```

and navigate to `http://localhost:4200` in your browser. You should see a very basic list of requested vacations.

#
# vacation-planner
