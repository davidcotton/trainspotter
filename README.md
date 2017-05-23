# Trainspotter
DJ tracklist management application. 


## Installation
1. Create the app & test databases
2. Add the environment variables to IDE/command line
3. Download dependencies, run database migrations and run the app
4. Load database fixtures to bootstrap the app with data

### Create the databases

    CREATE DATABASE `trainspotter` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    CREATE DATABASE `trainspotter_test` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

### Environment Variables
+ MYSQL_DB_USER - The MySQL DB user name
+ MYSQL_DB_PASS - The MySQL DB user password


### Download Dependencies and Run DB Migrations
  
    sbt run

### Load Fixtures
Run fixtures.sql


## Usage
Use SBT to run/compile the app.

    sbt run


## Testing
Use SBT to run tests.

    sbt test
