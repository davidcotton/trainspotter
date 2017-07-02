INSERT INTO `user` (`email`, `username`, `slug`, `status`, `karma`, `tracklists_created`, `tracks_identified`, `hash`, `salt`, `created`, `updated`) VALUES
  ('brian.mcgee@simpsons.com', 'Brian McGee', 'brian-mcgee', 'active', 12345, 31, 92423, '$2a$16$T1PaqXFutgw9qUmlK875Ge4wFRnn9TBMyJHfxyBpDXItcrNDL/OYa', '$2a$16$T1PaqXFutgw9qUmlK875Ge', NOW(), NOW()),
  ('rembrandt.q.einstein@simpsons.com', 'Rembrandt Q. Einstein', 'rembrandt-q-einstein', 'deleted', 12345, 49, 106389, '$2a$16$JzMtqiUzAsUkWn1AYe.1C.xKIJUcj9lInDBANSKNmiS5WCKW7uvai', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.', NOW(), NOW()),
  ('rory.b.bellows@simpsons.com', 'Rory B. Bellows', 'rory-b-bellows', 'unverified', 12345, 0, 1402, '$2a$16$aY..e8GAU2YGfdvLGqtaheWo5I7vwq9SPc7bqX8hgbgdSQEVUYGSq', '$2a$16$aY..e8GAU2YGfdvLGqtahe', NOW(), NOW());

INSERT INTO `artist` (`name`, `slug`, `image`, `description`, `status`, `created`, `updated`) VALUES
  ('John Digweed', 'john-digweed', 'john-digweed.jpg', '', 'active', NOW(), NOW()),
  ('Sasha', 'sasha', 'sasha.jpg', null, 'active', NOW(), NOW()),
  ('Adam Beyer', 'adam-beyer', 'adam-beyer.jpg', null, 'active', NOW(), NOW()),
  ('Solomun', 'solomun', null, null, 'active', NOW(), NOW()),
  ('Umek', 'umek', null, null, 'active', NOW(), NOW()),
  ('Marc Romboy', 'marc-romboy', null, null, 'active', NOW(), NOW());

INSERT INTO `channel` (`name`, `slug`, `image`, `description`, `status`, `created`, `updated`) VALUES
  ('Proton Radio', 'proton-radio', 'proton-opengraph.gif', 'Welcome to Proton, a subscriber supported Internet radio station and music label.', 'active', NOW(), NOW()),
  ('Triple J', 'triple-j', 'triple-j.jpg', '', 'active', NOW(), NOW()),
  ('BBC Radio 1', 'bbc-radio-1', 'bbc.jpg', 'Listen to BBC Radio 1, home of the Official Chart', 'active', NOW(), NOW());

INSERT INTO `genre` (`name`, `slug`, `status`, `created`, `updated`) VALUES
  ('House', 'house', 'active', NOW(), NOW()),
  ('Techno', 'techno', 'active', NOW(), NOW());

INSERT INTO `label` (`name`, `slug`, `image`, `description`, `status`, `created`, `updated`) VALUES
  ('Bedrock Records', 'bedrock-records', 'bedrock-records.jpg','Together John Digweed and Nick Muir are the key to what makes up the Bedrock record label tick. With the some of the cream of the progressive and breaks crop releases music on Bedrock the label constantly goes from strength to strength and provides the latest in quality beats. \nBedrock launched in 1999 with the classic progressive anthem Heaven Scent.', 'active', NOW(), NOW()),
  ('Last Night On Earth', 'last-night-on-earth', null, 'Label run by Sasha.', 'active', NOW(), NOW()),
  ('Drumcode', 'drumcode', 'drumcode.jpg', '', 'active', NOW(), NOW()),
  ('Get Wet', 'get-wet', null, '', 'active', NOW(), NOW()),
  ('Sinnbus', 'sinnbus', null, '', 'active', NOW(), NOW()),
  ('Mobilee Records', 'mobilee-records', 'mobilee-records.jpg', '', 'active', NOW(), NOW()),
  ('Souvenir', 'souvenir', null, '', 'active', NOW(), NOW()),
  ('Colour Series', 'colour-series', null, '', 'active', NOW(), NOW()),
  ('SCI+TEC', 'sci+tec', 'sci+tec.jpg', '', 'active', NOW(), NOW()),
  ('IAM Music', 'iam-music', null, '', 'active', NOW(), NOW()),
  ('Strange Idol', 'strange-idol', null, '', 'active', NOW(), NOW()),
  ('Ovum Recordings', 'ovum-recordings', 'ovum-recordings.jpg', 'Ovum has come to prominence with a certain Josh Wink amongst their ranks. A label with a very bright future, and who can argue, with artists such as Josh Wink, Pete Moss, DJ Dozia, David Alvarado and Rulers of the Deep.', 'active', NOW(), NOW()),
  ('Poker Flat Recordings', 'poker-flat-recordings', 'poker-flat-recordings.jpg', 'Martin Landsky, Steve Bug, Märtini Brös, and Orbit Starz make up a fair portion of the artists that release on the Poker Flat Recordings label.', 'active', NOW(), NOW()),
  ('Cocoon Recordings', 'cocoon-recordings', 'cocoon-recordings.jpg', 'Cocoon Recordings philosophy has its roots in the need to create a platform for young and talented DJs and producers to develop their personal path of creativity. Since 2000, the label has released more than 350 artists ranging from house and techno to minimal and tech-house sounds with a total of 300 releases.\n\nAlthough vinyl remains the main musical communication tool, Cocoon Recordings is also distributing its music on web portals like iTunes and Beatport (just to name a few) in order to reach a wider pool of followers and fans. Until now, more than 1.2 million tracks have been legally downloaded worldwide, especially in Germany, Japan, U.K., USA, Italy and Spain.', 'active', NOW(), NOW());

INSERT INTO `track` (`name`, `remix_name`, `genre_id`, `label_id`, `release_date`, `status`, `created`, `updated`)
VALUES
  ('Eros', null, 1, 1, NOW(), 'active', NOW(), NOW()),
  ('Eros', 'Deetron Dub Remix', 1, 1, NOW(), 'active', NOW(), NOW()),
  ('The Field And The Sun', null, 1, 3, NOW(), 'active', NOW(), NOW()),
  ('Tempest', 'Adam Port Europa Remix', 1, 4, NOW(), 'active', NOW(), NOW()),
  ('The Feeling', null, 2, 5, NOW(), 'active', NOW(), NOW()),
  ('Blackbox', 'Tiefschwarz & Yawk Remix', 2, 6, NOW(), 'active', NOW(), NOW()),
  ('Times', null, 2, 7, NOW(), 'active', NOW(), NOW()),
  ('Airplane', null, 2, 8, NOW(), 'active', NOW(), NOW()),
  ('You Can Relate', null, 2, 2, NOW(), 'active', NOW(), NOW()),
  ('Keep Faith', 'Scan Mode Remix', 2, 9, NOW(), 'active', NOW(), NOW());

INSERT INTO `tracklist` (`name`, `slug`, `performed`, `user_id`, `status`, `created`, `updated`) VALUES
  ('Sasha & John Digweed @ Carl Cox & Friends, Megastructure Stage, Ultra Music Festival Miami, MMW, United States', 'umf', '2017-03-25', 1, 'active', NOW(), NOW()),
  ('Transitions 657', 'transitions-657', '2017-03-31', 1, 'active', NOW(), NOW()),
  ('Adam Beyer @ Drumcode 354 (Metro City Perth, Australia 2017-04-24)', 'drumcode-354', '2017-05-18', 1, 'active', NOW(), NOW()),
  ('Sasha @ Last Night On Earth 025 (Coachella Festival, United States)', 'last-night-on-earth-025', '2017-05-29', 2, 'active', NOW(), NOW());

INSERT INTO `media` (`url`, `type`, `tracklist_id`, `artist_id`, `label_id`, `user_id`, `status`, `created`, `updated`) VALUES
  ('https://youtu.be/tI7ywh2sI04', 'youtube', 1, null, null, 1, 'active', NOW(), NOW()),
  ('https://www.mixcloud.com/globaldjmix/john-digweed-jesper-dahlback-transitions-664-2017-05-19/', 'mixcloud', 1, null, null, 1, 'active', NOW(), NOW()),
  ('https://soundcloud.com/john-digweed', 'soundcloud', null, 2, null, 2, 'active', NOW(), NOW());

INSERT INTO `program` (`name`, `slug`, `image`, `description`, `channel_id`, `status`, `created`, `updated`) VALUES
  ('Transitions', 'transitions', 'transitions.jpg', 'The most important radio show in house music needs no introduction, but here it goes anyway. John Digweed\'s long-standing weekly 2 hour program is legendary, airing for many years exclusively on Kiss 100 in London but now syndicated around the world and internet. In 2008, the show comes to Proton Radio. Expect upfront & unreleased tunes dished up by John Digweed alongside finely tuned guest mixes that are almost always exclusively prepared just for the show.', 1, 'active', NOW(), NOW()),
  ('Behind The Iron Curtain', 'behind-the-iron-curtain', 'behind-the-iron-curtain.jpg', 'Welcome to UMEK’s hour-long trip behind the iron curtain. Hear it here on KISS FM every Tuesday 5 – 6 am. It would take a long essay to fully explain UMEK’s meaning to electronic music. The Slovenian born producer and DJ has been setting trends and rocking dance floors for two decades now — and still shows no signs of slowing down. He’s tireless in his techno and tech house production and with more than 100 gigs per year, probably one of the busiest techno DJs out there.', 1, 'active', NOW(), NOW()),
  ('Diynamic', 'diynamic', 'diynamic.jpg', 'The weekly Diynamic radio show is hosted by Solomun & friends.', 1, 'active', NOW(), NOW()),
  ('Systematic Session', 'systematic-session', 'systematic.jpg', 'Systematic Recordings from Germany has now finally its own exclusive radio show. The label which is run by the worldwide well known DJ/ producer Marc Romboy has released dozens of trendsetting and forwardthinking electronic house tracks, contributed by artists like Stephan Bodzin, Steve Lawler, Booka Shade, Chelonis R. Jones, John Dahlback and Robert Babicz, just to name a few. The show is called "Systematic Sessions" and serves you one hour of Dj mixed up-to-date records by their host Marc Romboy and other globally profiled DJs and producers. You want to know more about Marc Romboy and Systematic recordings?', 1, 'active', NOW(), NOW()),
  ('House Party', 'house-party', null, 'Shake your house every Saturday night with the best party music, remixes, indie hits and classic cuts', 2, 'active', NOW(), NOW());
