# Trainspotter
DJ tracklist management application. 


## Installation
1. Install encryption package `brew install libsodium`
2. Create the app & test databases
3. Add the environment variables to IDE/command line
4. Download dependencies, run database migrations and run the app
5. Load database fixtures to bootstrap the app with data

### Create the databases

    CREATE DATABASE `trainspotter` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    CREATE DATABASE `trainspotter_test` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

### Environment Variables
+ MYSQL_DB_USER - The MySQL DB user name
+ MYSQL_DB_PASS - The MySQL DB user password
+ CRYPTO_SECRET - Used to sign the session cookie


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
