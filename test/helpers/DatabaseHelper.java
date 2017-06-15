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
    Ebean.execute(Ebean.createCallableSql(
        "INSERT INTO `artist` (`name`, `image`, `description`, `created`, `updated`)\n"
      + "VALUES\n"
      + "  ('John Digweed', 'john-digweed.jpg','' , NOW(), NOW()),\n"
      + "  ('Sasha', 'sasha.jpg','' , NOW(), NOW()),\n"
      + "  ('Adam Beyer', 'adam-beyer.jpg','' , NOW(), NOW());"
    ));
    Ebean.execute(Ebean.createCallableSql(
        "INSERT INTO `channel` (`name`, `image`, `description`, `created`, `updated`) VALUES\n"
      + "  ('Proton Radio', 'proton-opengraph.gif','Welcome to Proton, a subscriber supported Internet radio station and music label.' , NOW(), NOW()),\n"
      + "  ('Triple J', 'triple-j.jpg','' , NOW(), NOW()),\n"
      + "  ('BBC Radio 1', 'bbc.jpg','' , NOW(), NOW()),\n"
      + "  ('Kiss FM', 'kiss-fm.jpg','' , NOW(), NOW()),\n"
      + "  ('Energia 97 FM', null,'' , NOW(), NOW());"
    ));
//    Ebean.execute(Ebean.createCallableSql(""));
//    Ebean.execute(Ebean.createCallableSql(""));
//    Ebean.execute(Ebean.createCallableSql(""));
//    Ebean.execute(Ebean.createCallableSql(""));
  }

  public void clean(Database database) {
    Evolutions.cleanupEvolutions(database);
    database.shutdown();
  }
}
