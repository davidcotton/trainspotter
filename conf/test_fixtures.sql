INSERT INTO `user` (`email`, `hash`, `salt`, `display_name`, `status`, `created`, `updated`) VALUES
  ('brian.mcgee@simpsons.com', '$2a$16$T1PaqXFutgw9qUmlK875Ge4wFRnn9TBMyJHfxyBpDXItcrNDL/OYa', '$2a$16$T1PaqXFutgw9qUmlK875Ge', 'Brian McGee', 'active', NOW(), NOW()),
  ('rembrandt.q.einstein@simpsons.com', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.xKIJUcj9lInDBANSKNmiS5WCKW7uvai', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.', 'Rembrandt Q. Einstein', 'deleted', NOW(), NOW()),
  ('rory.b.bellows@simpsons.com', '$2a$16$aY..e8GAU2YGfdvLGqtaheWo5I7vwq9SPc7bqX8hgbgdSQEVUYGSq', '$2a$16$aY..e8GAU2YGfdvLGqtahe', 'Rory B. Bellows', 'unverified', NOW(), NOW());

INSERT INTO `artist` (`name`, `slug`, `image`, `description`, `created`, `updated`) VALUES
  ('John Digweed', 'john-digweed', 'john-digweed.jpg', '' , NOW(), NOW()),
  ('Sasha', 'sasha', 'sasha.jpg', null , NOW(), NOW()),
  ('Adam Beyer', 'adam-beyer', 'adam-beyer.jpg', null , NOW(), NOW()),
  ('Solomun', 'solomun', null, null , NOW(), NOW()),
  ('Umek', 'umek', null, null , NOW(), NOW()),
  ('Marc Romboy', 'marc-romboy', null, null , NOW(), NOW());

INSERT INTO `channel` (`name`, `slug`, `image`, `description`, `created`, `updated`) VALUES
  ('Proton Radio', 'proton-radio', 'proton-opengraph.gif', 'Welcome to Proton, a subscriber supported Internet radio station and music label.' , NOW(), NOW()),
  ('Triple J', 'triple-j', 'triple-j.jpg', '', NOW(), NOW()),
  ('BBC Radio 1', 'bbc-radio-1', 'bbc.jpg', 'Listen to BBC Radio 1, home of the Official Chart' , NOW(), NOW());

INSERT INTO `genre` (`name`, `slug`, `created`, `updated`) VALUES
  ('House', 'house', NOW(), NOW()),
  ('Techno', 'techno', NOW(), NOW());

INSERT INTO `label` (`name`, `slug`, `image`, `description`, `created`, `updated`) VALUES
  ('Bedrock Records', 'bedrock-records', 'bedrock-records.jpg','Together John Digweed and Nick Muir are the key to what makes up the Bedrock record label tick. With the some of the cream of the progressive and breaks crop releases music on Bedrock the label constantly goes from strength to strength and provides the latest in quality beats. \nBedrock launched in 1999 with the classic progressive anthem Heaven Scent.', NOW(), NOW()),
  ('Last Night On Earth', 'last-night-on-earth', null, 'Label run by Sasha.', NOW(), NOW()),
  ('Drumcode', 'drumcode', 'drumcode.jpg', '', NOW(), NOW());

INSERT INTO `track` (`name`, `remix_name`, `genre_id`, `label_id`, `release_date`, `created`, `updated`)
VALUES
  ('Eros', null, 1, 1, NOW(), NOW(), NOW()),
  ('Eros', 'Deetron Dub Remix', 1, 1, NOW(), NOW(), NOW()),
  ('The Field And The Sun', null, 1, 3, NOW(), NOW(), NOW()),
  ('Tempest', 'Adam Port Europa Remix', 1, 4, NOW(), NOW(), NOW()),
  ('The Feeling', null, 2, 5, NOW(), NOW(), NOW()),
  ('Blackbox', 'Tiefschwarz & Yawk Remix', 2, 6, NOW(), NOW(), NOW()),
  ('Times', null, 2, 7, NOW(), NOW(), NOW()),
  ('Airplane', null, 2, 8, NOW(), NOW(), NOW()),
  ('You Can Relate', null, 2, 2, NOW(), NOW(), NOW()),
  ('Keep Faith', 'Scan Mode Remix', 2, 9, NOW(), NOW(), NOW());

INSERT INTO `tracklist` (`name`, `slug`, `performed`, `user_id`, `created`, `updated`) VALUES
  ('Sasha & John Digweed @ Carl Cox & Friends, Megastructure Stage, Ultra Music Festival Miami, MMW, United States', 'umf', '2017-03-25', 1, NOW(), NOW()),
  ('Transitions 657', 'transitions-657', '2017-03-31', 1, NOW(), NOW()),
  ('Adam Beyer @ Drumcode 354 (Metro City Perth, Australia 2017-04-24)', 'drumcode-354', '2017-05-18', 1, NOW(), NOW()),
  ('Sasha @ Last Night On Earth 025 (Coachella Festival, United States)', 'last-night-on-earth-025', '2017-05-29', 2, NOW(), NOW());

INSERT INTO `media` (`url`, `tracklist_id`, `artist_id`, `label_id`, `created`, `updated`) VALUES
  ('https://youtu.be/tI7ywh2sI04', 1, null, null, NOW(), NOW()),
  ('https://www.mixcloud.com/globaldjmix/john-digweed-jesper-dahlback-transitions-664-2017-05-19/', 1, null, null, NOW(), NOW()),
  ('https://soundcloud.com/john-digweed', null, 2, null, NOW(), NOW());

INSERT INTO `program` (`name`, `slug`, `image`, `description`, `channel_id`, `created`, `updated`) VALUES
  ('Transitions', 'transitions', 'transitions.jpg', 'The most important radio show in house music needs no introduction, but here it goes anyway. John Digweed\'s long-standing weekly 2 hour program is legendary, airing for many years exclusively on Kiss 100 in London but now syndicated around the world and internet. In 2008, the show comes to Proton Radio. Expect upfront & unreleased tunes dished up by John Digweed alongside finely tuned guest mixes that are almost always exclusively prepared just for the show.', 1, NOW(), NOW()),
  ('Behind The Iron Curtain', 'behind-the-iron-curtain', 'behind-the-iron-curtain.jpg', 'Welcome to UMEK’s hour-long trip behind the iron curtain. Hear it here on KISS FM every Tuesday 5 – 6 am. It would take a long essay to fully explain UMEK’s meaning to electronic music. The Slovenian born producer and DJ has been setting trends and rocking dance floors for two decades now — and still shows no signs of slowing down. He’s tireless in his techno and tech house production and with more than 100 gigs per year, probably one of the busiest techno DJs out there.', 1, NOW(), NOW()),
  ('Diynamic', 'diynamic', 'diynamic.jpg', 'The weekly Diynamic radio show is hosted by Solomun & friends.', 1, NOW(), NOW()),
  ('Systematic Session', 'systematic-session', 'systematic.jpg', 'Systematic Recordings from Germany has now finally its own exclusive radio show. The label which is run by the worldwide well known DJ/ producer Marc Romboy has released dozens of trendsetting and forwardthinking electronic house tracks, contributed by artists like Stephan Bodzin, Steve Lawler, Booka Shade, Chelonis R. Jones, John Dahlback and Robert Babicz, just to name a few. The show is called "Systematic Sessions" and serves you one hour of Dj mixed up-to-date records by their host Marc Romboy and other globally profiled DJs and producers. You want to know more about Marc Romboy and Systematic recordings? ', 1, NOW(), NOW()),
  ('House Party', 'house-party', null, 'Shake your house every Saturday night with the best party music, remixes, indie hits and classic cuts', 2, NOW(), NOW());
