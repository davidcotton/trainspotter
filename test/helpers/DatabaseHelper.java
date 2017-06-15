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
        "INSERT INTO `user` (`email`, `hash`, `salt`, `display_name`, `status`, `created`, `updated`) VALUES\n"
      + "  ('brian.mcgee@simpsons.com', '$2a$16$T1PaqXFutgw9qUmlK875Ge4wFRnn9TBMyJHfxyBpDXItcrNDL/OYa', '$2a$16$T1PaqXFutgw9qUmlK875Ge', 'Brian McGee', 'active', NOW(), NOW()),\n"
      + "  ('rembrandt.q.einstein@simpsons.com', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.xKIJUcj9lInDBANSKNmiS5WCKW7uvai', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.', 'Rembrandt Q. Einstein', 'deleted', NOW(), NOW()),\n"
      + "  ('rory.b.bellows@simpsons.com', '$2a$16$aY..e8GAU2YGfdvLGqtaheWo5I7vwq9SPc7bqX8hgbgdSQEVUYGSq', '$2a$16$aY..e8GAU2YGfdvLGqtahe', 'Rory B. Bellows', 'unverified', NOW(), NOW());\n"
      + "\n"
    ));
    Ebean.execute(Ebean.createCallableSql(
        "INSERT INTO `artist` (`name`, `image`, `description`, `created`, `updated`) VALUES\n"
      + "  ('John Digweed', 'john-digweed.jpg','' , NOW(), NOW()),\n"
      + "  ('Sasha', 'sasha.jpg','' , NOW(), NOW()),\n"
      + "  ('Adam Beyer', 'adam-beyer.jpg','' , NOW(), NOW());"
    ));
    Ebean.execute(Ebean.createCallableSql(
        "INSERT INTO `channel` (`name`, `image`, `description`, `created`, `updated`) VALUES\n"
      + "  ('Proton Radio', 'proton-opengraph.gif','Welcome to Proton, a subscriber supported Internet radio station and music label.' , NOW(), NOW()),\n"
      + "  ('Triple J', 'triple-j.jpg','' , NOW(), NOW());"
    ));
    Ebean.execute(Ebean.createCallableSql(
      "INSERT INTO `genre` (name, created, updated) VALUES\n"
      + "  ('House', NOW(), NOW()),\n"
      + "  ('Techno', NOW(), NOW());"));
    Ebean.execute(Ebean.createCallableSql(
      "INSERT INTO `label` (`name`, `image`, `description`, `created`, `updated`) VALUES\n"
      + "  ('Bedrock Records', 'bedrock-records.jpg','Together John Digweed and Nick Muir are the key to what makes up the Bedrock record label tick. With the some of the cream of the progressive and breaks crop releases music on Bedrock the label constantly goes from strength to strength and provides the latest in quality beats. \nBedrock launched in 1999 with the classic progressive anthem Heaven Scent.', NOW(), NOW()),\n"
      + "  ('Last Night On Earth', null, 'Label run by Sasha.', NOW(), NOW()),\n"
      + "  ('Drumcode', 'drumcode.jpg', '', NOW(), NOW());"
    ));
    Ebean.execute(Ebean.createCallableSql(
      "INSERT INTO `tracklist` (`name`, `performed`, `user_id`, `created`, `updated`) VALUES\n"
      + "  ('Sasha & John Digweed @ Carl Cox & Friends, Megastructure Stage, Ultra Music Festival Miami, MMW, United States', '2017-03-25', 1, NOW(), NOW()),\n"
      + "  ('John Digweed & Joeski - Transitions 657', '2017-03-31', 1, NOW(), NOW()),\n"
      + "  ('Adam Beyer @ Drumcode 354 (Metro City Perth, Australia 2017-04-24)', '2017-05-18', 1, NOW(), NOW()),\n"
      + "  ('Sasha @ Last Night On Earth 025 (Coachella Festival, United States)', '2017-05-29', 2, NOW(), NOW());"
    ));
    Ebean.execute(Ebean.createCallableSql(
      "INSERT INTO `media` (url, tracklist_id, artist_id, label_id, created, updated) VALUES\n"
      + "  ('https://youtu.be/tI7ywh2sI04', 1, null, null, NOW(), NOW()),\n"
      + "  ('https://www.mixcloud.com/globaldjmix/john-digweed-jesper-dahlback-transitions-664-2017-05-19/', 1, null, null, NOW(), NOW()),\n"
      + "  ('https://soundcloud.com/john-digweed', null, 2, null, NOW(), NOW());"
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
