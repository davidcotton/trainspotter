package repositories;

import static play.inject.Bindings.bind;

import com.google.inject.Guice;
import helpers.DatabaseHelper;
import org.junit.After;
import org.junit.Before;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.Mode;
import play.db.Database;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.test.WithApplication;

abstract public class AbstractIntegrationTest extends WithApplication {

  private DatabaseHelper databaseHelper;
  private Database database;

  @Override
  protected Application provideApplication() {
    databaseHelper = new DatabaseHelper();
    database = databaseHelper.init();

    GuiceApplicationBuilder builder = new GuiceApplicationLoader()
        .builder(new ApplicationLoader.Context(Environment.simple()));

    Guice.createInjector(builder.applicationModule()).injectMembers(this);

    return builder
        .overrides(bind(Database.class).toInstance(database))
//        .configure("db.default.driver", "com.mysql.jdbc.Driver")
//        .configure("db.default.url", System.getenv("MYSQL_DB_URL"))
//        .configure("db.default.username", System.getenv("MYSQL_DB_USER"))
//        .configure("db.default.password", System.getenv("MYSQL_DB_PASS"))
        .in(Mode.TEST)
        .build();
  }

  @Before
  public void setUp() {
    databaseHelper.load(database);
  }

  @After
  public void tearDown() {
    databaseHelper.clean(database);
  }
}
