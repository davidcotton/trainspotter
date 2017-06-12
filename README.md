# Trainspotter
DJ tracklist management application. 


## Installation
1. Install encryption package `brew install libsodium` (required by Kalium)
2. Create the app & test databases
3. Add the environment variables to IDE/command line
4. Download dependencies, run database migrations and run the app
5. Load database fixtures to bootstrap the app with data

### Create the app & test databases

    CREATE DATABASE `trainspotter` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    CREATE DATABASE `trainspotter_test` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

### Environment Variables
+ MYSQL_DB_USER - The MySQL DB user name, e.g. `root`
+ MYSQL_DB_PASS - The MySQL DB user password e.g. `password`
+ MYSQL_DB_URL - The MySQL DB connection string, e.g. `jdbc:mysql://localhost:3306/trainspotter`
+ CRYPTO_SECRET - Used to sign the HMAC, must be 32 characters or will throw invalid size exception.


### Download Dependencies and Run DB Migrations

    sbt update

### Load Fixtures
Run fixtures.sql


## Usage
Use SBT to run/compile the app.

    sbt run


## Testing
Use SBT to run tests.

    sbt test
