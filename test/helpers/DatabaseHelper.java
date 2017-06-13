package helpers;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;

public class DatabaseHelper {

  private Database database;

  public void init() {
    database = Databases.createFrom(
        "com.mysql.jdbc.Driver",
        "",
        ImmutableMap.of("username", "")
    );
//    database = Databases.inMemory(
//        "default",
//        ImmutableMap.of("MODE", "MYSQL"),
//        ImmutableMap.of("logStatements", true)
//    );
  }

  public void load() {
    Evolutions.applyEvolutions(database);
    Ebean.execute(Ebean.createCallableSql(
        "INSERT INTO `user` (`email`, `hash`, `salt`, `display_name`, `status`, `created`, `updated`)\n" +
            "VALUES\n" +
            "  ('john.digweed@bedrock-records.com', '$2a$16$T1PaqXFutgw9qUmlK875Ge4wFRnn9TBMyJHfxyBpDXItcrNDL/OYa', '$2a$16$T1PaqXFutgw9qUmlK875Ge', 'John Digweed', 'active', NOW(), NOW()),\n" +
            "  ('sasha@last-night-on-earth.com', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.xKIJUcj9lInDBANSKNmiS5WCKW7uvai', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.', 'Sasha', 'deleted', NOW(), NOW()),\n" +
            "  ('adam.beyer@drumcode.com', '$2a$16$aY..e8GAU2YGfdvLGqtaheWo5I7vwq9SPc7bqX8hgbgdSQEVUYGSq', '$2a$16$aY..e8GAU2YGfdvLGqtahe', 'Adam Beyer', 'active', NOW(), NOW())\n" +
            ";"
    ));
  }

  public void clean() {
    Evolutions.cleanupEvolutions(database);
    database.shutdown();
  }

  public Database getDatabase() {
    return database;
  }
}
