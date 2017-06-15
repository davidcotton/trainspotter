package helpers;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;

public class DatabaseHelper {

  public Database init() {
    return Databases.createFrom(
        "com.mysql.jdbc.Driver",
        System.getenv("MYSQL_DB_URL"),
        ImmutableMap.of(
            "username", System.getenv("MYSQL_DB_USER"),
            "password", System.getenv("MYSQL_DB_PASS")
        )
    );
  }

  public void load(Database database) {
    Evolutions.applyEvolutions(database);
    Ebean.execute(Ebean.createCallableSql(
        "INSERT INTO `user` (`email`, `hash`, `salt`, `display_name`, `status`, `created`, `updated`)\n"
      + "VALUES\n"
      + "  ('brian.mcgee@simpsons.com', '$2a$16$T1PaqXFutgw9qUmlK875Ge4wFRnn9TBMyJHfxyBpDXItcrNDL/OYa', '$2a$16$T1PaqXFutgw9qUmlK875Ge', 'Brian McGee', 'active', NOW(), NOW()),\n"
      + "  ('rembrandt.q.einstein@simpsons.com', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.xKIJUcj9lInDBANSKNmiS5WCKW7uvai', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.', 'Rembrandt Q. Einstein', 'deleted', NOW(), NOW()),\n"
      + "  ('rory.b.bellows@simpsons.com', '$2a$16$aY..e8GAU2YGfdvLGqtaheWo5I7vwq9SPc7bqX8hgbgdSQEVUYGSq', '$2a$16$aY..e8GAU2YGfdvLGqtahe', 'Rory B. Bellows', 'unverified', NOW(), NOW());\n"
      + "\n"
    ));
  }

  public void clean(Database database) {
    Evolutions.cleanupEvolutions(database);
    database.shutdown();
  }
}
