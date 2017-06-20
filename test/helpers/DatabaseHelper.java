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
      + "  ('John Digweed', 'john-digweed.jpg', '' , NOW(), NOW()),\n"
      + "  ('Sasha', 'sasha.jpg', null , NOW(), NOW()),\n"
      + "  ('Adam Beyer', 'adam-beyer.jpg', null , NOW(), NOW()),\n"
      + "  ('Solomun', null, null , NOW(), NOW()),\n"
      + "  ('Umek', null, null , NOW(), NOW()),\n"
      + "  ('Marc Romboy', null, null , NOW(), NOW());"
    ));
    Ebean.execute(Ebean.createCallableSql(
        "INSERT INTO `channel` (`name`, `image`, `description`, `created`, `updated`) VALUES\n"
      + "  ('Proton Radio', 'proton-opengraph.gif','Welcome to Proton, a subscriber supported Internet radio station and music label.' , NOW(), NOW()),\n"
      + "  ('Triple J', 'triple-j.jpg','' , NOW(), NOW()),\n"
      + "  ('BBC Radio 1', 'bbc.jpg', 'Listen to BBC Radio 1, home of the Official Chart, the Live Lounge and the world''s greatest DJs including Nick Grimshaw, Scott Mills, Clara Amfo, Greg James, Annie Mac and many more. Listen on DAB Radio, Digital TV, 97-99 FM, Online and Mobile.' , NOW(), NOW());"
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
    Ebean.execute(Ebean.createCallableSql(
      "INSERT INTO `program` (name, image, description, channel_id, created, updated) VALUES\n"
      + "  ('Transitions', 'transitions.jpg', 'The most important radio show in house music needs no introduction, but here it goes anyway. John Digweed\\'s long-standing weekly 2 hour program is legendary, airing for many years exclusively on Kiss 100 in London but now syndicated around the world and internet. In 2008, the show comes to Proton Radio. Expect upfront & unreleased tunes dished up by John Digweed alongside finely tuned guest mixes that are almost always exclusively prepared just for the show.', 1, NOW(), NOW()),\n"
      + "  ('Behind The Iron Curtain', 'behind-the-iron-curtain.jpg', 'Welcome to UMEK’s hour-long trip behind the iron curtain. Hear it here on KISS FM every Tuesday 5 – 6 am. It would take a long essay to fully explain UMEK’s meaning to electronic music. The Slovenian born producer and DJ has been setting trends and rocking dance floors for two decades now — and still shows no signs of slowing down. He’s tireless in his techno and tech house production and with more than 100 gigs per year, probably one of the busiest techno DJs out there.', 1, NOW(), NOW()),\n"
      + "  ('Diynamic', 'diynamic.jpg', 'The weekly Diynamic radio show is hosted by Solomun & friends.', 1, NOW(), NOW()),\n"
      + "  ('Systematic Session', 'systematic.jpg', 'Systematic Recordings from Germany has now finally its own exclusive radio show. The label which is run by the worldwide well known DJ/ producer Marc Romboy has released dozens of trendsetting and forwardthinking electronic house tracks, contributed by artists like Stephan Bodzin, Steve Lawler, Booka Shade, Chelonis R. Jones, John Dahlback and Robert Babicz, just to name a few. The show is called \"Systematic Sessions\" and serves you one hour of Dj mixed up-to-date records by their host Marc Romboy and other globally profiled DJs and producers. You want to know more about Marc Romboy and Systematic recordings? ', 1, NOW(), NOW()),\n"
      + "  ('House Party', null, 'Shake your house every Saturday night with the best party music, remixes, indie hits and classic cuts', 2, NOW(), NOW());"
    ));
//    Ebean.execute(Ebean.createCallableSql(""));
//    Ebean.execute(Ebean.createCallableSql(""));
//    Ebean.execute(Ebean.createCallableSql(""));
  }

  public void clean(Database database) {
    Evolutions.cleanupEvolutions(database);
    database.shutdown();
  }
}
