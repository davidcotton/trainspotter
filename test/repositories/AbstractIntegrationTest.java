package repositories;

import static play.inject.Bindings.bind;

import com.avaje.ebean.Ebean;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import javax.persistence.PersistenceException;
import org.junit.After;
import org.junit.Before;
import play.Application;
import play.Logger;
import play.Mode;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

abstract public class AbstractIntegrationTest extends WithApplication {

  private Database database;
  private static final String CONFIG = "application.conf";
  private static final String FIXTURES = "test_fixtures.sql";


  @Override
  protected Application provideApplication() {
    database = Databases.createFrom(
        "com.mysql.jdbc.Driver",
        System.getenv("MYSQL_DB_URL"),
        ImmutableMap.of(
            "username", System.getenv("MYSQL_DB_USER"),
            "password", System.getenv("MYSQL_DB_PASS")
        )
    );

    return new GuiceApplicationBuilder()
//        .overrides(bind(Database.class).toInstance(database))
//        .in(new Environment(new File(CONFIG), this.getClass().getClassLoader(), Mode.TEST))
        .in(Mode.TEST)
        .build();
  }

  @Before
  public void setUp() throws IOException, RuntimeException {
    // run DB migrations
    Evolutions.applyEvolutions(database);
    // read fixtures in
    String fixtures = Files.toString(new File(app.classloader().getResource(FIXTURES).getFile()), Charsets.UTF_8);
    // load fixtures
    try {
      for (String sql: fixtures.split("\\n[\\n]+")) {
        Ebean.execute(Ebean.createCallableSql(sql));
      }
    } catch (PersistenceException e) {
      // if fixtures fail to load, the test database will be left in an inconsistent state
      // we'll just drop it and start again
      Ebean.execute(Ebean.createCallableSql("DROP DATABASE trainspotter_test;"));
      Ebean.execute(Ebean.createCallableSql("CREATE DATABASE `trainspotter_test` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"));
      database.shutdown();
      Logger.error("Error loading fixtures");
      throw new RuntimeException(e.getMessage());
    }
  }

  @After
  public void tearDown() {
    // empty the DB
    Evolutions.cleanupEvolutions(database);
    // close the connection
    database.shutdown();
  }
}
