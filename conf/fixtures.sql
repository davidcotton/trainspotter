
INSERT INTO `user` (`email`, `username`, `slug`, `status`, `karma`, `tracklists_created`, `tracks_identified`, `hash`, `salt`, `created`, `updated`) VALUES
  ('brian.mcgee@simpsons.com', 'Brian McGee', 'brian-mcgee', 'active', 12345, 31, 92423, '$2a$16$T1PaqXFutgw9qUmlK875Ge4wFRnn9TBMyJHfxyBpDXItcrNDL/OYa', '$2a$16$T1PaqXFutgw9qUmlK875Ge', NOW(), NOW()),
  ('rembrandt.q.einstein@simpsons.com', 'Rembrandt Q. Einstein', 'rembrandt-q-einstein', 'deleted', 12345, 49, 106389, '$2a$16$JzMtqiUzAsUkWn1AYe.1C.xKIJUcj9lInDBANSKNmiS5WCKW7uvai', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.', NOW(), NOW()),
  ('rory.b.bellows@simpsons.com', 'Rory B. Bellows', 'rory-b-bellows', 'unverified', 12345, 0, 1402, '$2a$16$aY..e8GAU2YGfdvLGqtaheWo5I7vwq9SPc7bqX8hgbgdSQEVUYGSq', '$2a$16$aY..e8GAU2YGfdvLGqtahe', NOW(), NOW());

# INSERT INTO `user_permission` (id, permission_value) VALUES
#   (),
#   ();

INSERT INTO `artist` (`name`, `slug`, `image`, `description`, `status`, `created`, `updated`)
VALUES
  ('Sasha', 'sasha', 'sasha.jpg', null, 'active', NOW(), NOW()),
  ('John Digweed', 'john-digweed', 'john-digweed.jpg', null, 'active', NOW(), NOW()),
  ('Customer', 'customer', null, null, 'active', NOW(), NOW()),
  ('Deetron', 'deetron', 'deetron.jpg', null, 'active', NOW(), NOW()),
  ('Kaled', 'kaled', null, null, 'active', NOW(), NOW()),
  ('Lessons', 'lessons', null, null, 'active', NOW(), NOW()),
  ('Adam Port', 'adam-port', 'adam-port.jpg', null, 'active', NOW(), NOW()),
  ('Re.You', 're.you', 're.you.jpg', null, 'active', NOW(), NOW()),
  ('Tiefschwarz', 'tiefschwarz', null, null, 'active', NOW(), NOW()),
  ('Yawk', 'yawk', null, null, 'active', NOW(), NOW()),
  ('Soame', 'soame', null, null, 'active', NOW(), NOW()),
  ('Wigbert', 'wigbert', null, null, 'active', NOW(), NOW()),
  ('Citizen', 'citizen', null, null, 'active', NOW(), NOW()),
  ('Kocleo', 'kocleo', null, null, 'active', NOW(), NOW()),
  ('Scan Mode', 'scan-mode', null, null, 'active', NOW(), NOW()),
  ('Jesper Dahlback', 'jesper-dahlback', null, null, 'active', NOW(), NOW()),
  ('Umek', 'umek', 'umek.jpg', 'For a long time, Slovenia is known for its great electronic parties, but as the local scene is of boutique dimensions, everyone, who wishes to take the art of mixing and producing to a professional level, has to go out there and enter the worldwide scene. Umek has done this step as early as in the middle of the 90s and so he became one of the most important techno players of the global techno scene, even before the end of the last millennium. Together with people, who shared the same views, he successfully captured the first era of researching and recreating the rules of the dance music in Recycled Loops, Consumer Recreation and Astrodisco platforms, which all put the sound of the Slovenian techno on the world map of electronic music.', 'active', NOW(), NOW()),
  ('Solomun', 'solomun', 'solomun.jpg', null, 'active', NOW(), NOW()),
  ('Marc Romboy', 'marc-romboy', 'marc-romboy.jpg', null, 'active', NOW(), NOW()),
  ('Henry Saiz', 'henry-saiz', 'henry-saiz.jpg', null, 'active', NOW(), NOW()),
  ('Adam Beyer', 'adam-beyer', 'adam-beyer.jpg', null, 'active', NOW(), NOW()),
  ('Carl Cox', 'carl-cox', 'carl-cox.jpg', null, 'active', NOW(), NOW()),
  ('Joeski', 'joeski', 'joeski.jpg', null, 'active', NOW(), NOW()),
  ('Quivver', 'quivver', null, null, 'active', NOW(), NOW()),
  ('Jamie Anderson', 'jamie-anderson', 'jamie-anderson.jpg', null, 'active', NOW(), NOW()),
  ('Jimmy Van M', 'jimmy-van-m', 'jimmy-van-m.jpg', null, 'active', NOW(), NOW()),
  ('Undo', 'undo', null, null, 'active', NOW(), NOW()),
  ('Harvey McKay', 'harvey-mckay', 'harvey-mckay.jpg', null, 'active', NOW(), NOW()),
  ('Dance Spirit', 'dance-spirit', 'dance-spirit.jpg', null, 'active', NOW(), NOW()),
  ('DJ Three', 'dj-three', null, null, 'active', NOW(), NOW()),
  ('Jeremy Olander', 'jeremy-olander', 'jeremy-olander.jpg', null, 'active', NOW(), NOW()),
  ('Öona Dahl', 'oona-dahl', null, null, 'active', NOW(), NOW()),
  ('Pete Tong', 'pete-tong', 'pete-tong.jpg', null, 'active', NOW(), NOW());

INSERT INTO `channel` (`name`, `slug`, `image`, `description`, `status`, `created`, `updated`) VALUES
  ('Proton Radio', 'proton-radio', 'proton-opengraph.gif','Welcome to Proton, a subscriber supported Internet radio station and music label.', 'active', NOW(), NOW()),
  ('Triple J', 'triple-j', null, 'triple j is the national youth broadcaster for young Australians. We aim to bring you the latest, greatest music and the stories that matter on the radio (covering over 98% of the nation), online, on your phone and in print with J Annual.', 'active', NOW(), NOW()),
  ('BBC Radio 1', 'bbc-radio-1', null, 'Listen to BBC Radio 1, home of the Official Chart, the Live Lounge and the world''s greatest DJs including Nick Grimshaw, Scott Mills, Clara Amfo, Greg James, Annie Mac and many more. Listen on DAB Radio, Digital TV, 97-99 FM, Online and Mobile.', 'active', NOW(), NOW()),
  ('KISS FM', 'kiss-fm', null, 'Kiss is a UK radio station which broadcasts on FM and National DAB and specialises in pop, hip hop, R&B, urban and electronic dance music.', 'active', NOW(), NOW()),
  ('Energia 97 FM', 'energia-97-fm', null, '', 'active', NOW(), NOW());

INSERT INTO `label` (`name`, `slug`, `image`, `description`, `status`, `created`, `updated`)
VALUES
  ('Not on label', 'not-on-label', null, '', 'active', NOW(), NOW()),
  ('Bedrock Records', 'bedrock-records', 'bedrock-records.jpg', 'Together John Digweed and Nick Muir are the key to what makes up the Bedrock record label tick. With the some of the cream of the progressive and breaks crop releases music on Bedrock the label constantly goes from strength to strength and provides the latest in quality beats. \nBedrock launched in 1999 with the classic progressive anthem Heaven Scent.', 'active', NOW(), NOW()),
  ('Get Wet', 'get-wet', null, '', 'active', NOW(), NOW()),
  ('Sinnbus', 'sinnbus', null, '', 'active', NOW(), NOW()),
  ('Mobilee Records', 'mobilee-records', 'mobilee-records.jpg', '', 'active', NOW(), NOW()),
  ('Souvenir', 'souvenir', null, '', 'active', NOW(), NOW()),
  ('Colour Series', 'colour-series', null, '', 'active', NOW(), NOW()),
  ('SCI+TEC', 'sci+tec', 'sci+tec.jpg', '', 'active', NOW(), NOW()),
  ('IAM Music', 'iam-music', null, '', 'active', NOW(), NOW()),
  ('Strange Idol', 'strange-idol', null, '', 'active', NOW(), NOW()),
  ('Last Night On Earth', 'last-night-on-earth', 'last-night-on-earth.jpg', 'Label run by Sasha', 'active', NOW(), NOW()),
  ('Drumcode', 'drumcode', 'drumcode.jpg', 'Techno label run by Adam Beyer.', 'active', NOW(), NOW()),
  ('Ovum Recordings', 'ovum-recordings', 'ovum-recordings.jpg', 'Ovum has come to prominence with a certain Josh Wink amongst their ranks. A label with a very bright future, and who can argue, with artists such as Josh Wink, Pete Moss, DJ Dozia, David Alvarado and Rulers of the Deep.', 'active', NOW(), NOW()),
  ('Poker Flat Recordings', 'poker-flat-recordings', 'poker-flat-recordings.jpg', 'Martin Landsky, Steve Bug, Märtini Brös, and Orbit Starz make up a fair portion of the artists that release on the Poker Flat Recordings label.', 'active', NOW(), NOW()),
  ('Cocoon Recordings', 'cocoon-recordings', 'cocoon-recordings.jpg', 'Cocoon Recordings philosophy has its roots in the need to create a platform for young and talented DJs and producers to develop their personal path of creativity. Since 2000, the label has released more than 350 artists ranging from house and techno to minimal and tech-house sounds with a total of 300 releases.\n\nAlthough vinyl remains the main musical communication tool, Cocoon Recordings is also distributing its music on web portals like iTunes and Beatport (just to name a few) in order to reach a wider pool of followers and fans. Until now, more than 1.2 million tracks have been legally downloaded worldwide, especially in Germany, Japan, U.K., USA, Italy and Spain.', 'active', NOW(), NOW()),
  ('Egothermia', 'egothermia', 'egothermia-records.jpg', null, 'active', NOW(), NOW()),
  ('Octopus Records', 'octopus-records', 'octopus-records.jpg', 'Octopus Recordings releases classic modern Techno from its base in Barcelona.Founded and ran by DJ and recording artist,Sian. http://octopusrecordings.com', 'active', NOW(), NOW()),
  ('Get Physical Music', 'get-physical-music', 'get-physical-music.jpg', 'The name says it all: Get Physical tracks could hardly be more moving and corporeal. This aspect of physicality also resurfaces on two of the labels key releases: the in-house DJ mix series "Body Language" and a string of double vinyl compilations called "Full Body Workout", brimming with new beats and sounds by up-and-coming talents.', 'active', NOW(), NOW());

INSERT INTO `genre` (`name`, `slug`, `status`, `created`, `updated`)
VALUES
  ('House', 'house', 'active', NOW(), NOW()),
  ('Techno', 'techno', 'active', NOW(), NOW()),
  ('Deep House', 'deep-house', 'active', NOW(), NOW()),
  ('Tech House', 'tech-house', 'active', NOW(), NOW()),
  ('Progressive House', 'progressive-house', 'active', NOW(), NOW()),
  ('Electronica', 'electronica', 'active', NOW(), NOW());

INSERT INTO `program` (`name`, `slug`, `image`, `description`, `channel_id`, `status`, `created`, `updated`) VALUES
  ('Transitions', 'transitions', 'transitions.jpg', 'The most important radio show in house music needs no introduction, but here it goes anyway. John Digweed\'s long-standing weekly 2 hour program is legendary, airing for many years exclusively on Kiss 100 in London but now syndicated around the world and internet. In 2008, the show comes to Proton Radio. Expect upfront & unreleased tunes dished up by John Digweed alongside finely tuned guest mixes that are almost always exclusively prepared just for the show.', 1, 'active', NOW(), NOW()),
  ('Behind The Iron Curtain', 'behind-the-iron-curtain', 'behind-the-iron-curtain.jpg', 'Welcome to UMEK’s hour-long trip behind the iron curtain. Hear it here on KISS FM every Tuesday 5 – 6 am. It would take a long essay to fully explain UMEK’s meaning to electronic music. The Slovenian born producer and DJ has been setting trends and rocking dance floors for two decades now — and still shows no signs of slowing down. He’s tireless in his techno and tech house production and with more than 100 gigs per year, probably one of the busiest techno DJs out there.', 1, 'active', NOW(), NOW()),
  ('Diynamic', 'diynamic', 'diynamic.jpg', 'The weekly Diynamic radio show is hosted by Solomun & friends.', 1, 'active', NOW(), NOW()),
  ('Systematic Session', 'systematic-session', 'systematic.jpg', 'Systematic Recordings from Germany has now finally its own exclusive radio show. The label which is run by the worldwide well known DJ/ producer Marc Romboy has released dozens of trendsetting and forwardthinking electronic house tracks, contributed by artists like Stephan Bodzin, Steve Lawler, Booka Shade, Chelonis R. Jones, John Dahlback and Robert Babicz, just to name a few. The show is called "Systematic Sessions" and serves you one hour of Dj mixed up-to-date records by their host Marc Romboy and other globally profiled DJs and producers. You want to know more about Marc Romboy and Systematic recordings?', 1, 'active', NOW(), NOW()),
  ('The Labyrinth', 'the-labyrinth', 'henry-saiz.jpg', 'Hi everybody, this is Henry Saiz, welcome to the labyrinth, my first online radio show. I would like to use this platform to share with you some ideas about music, I will try to develop a concept in every show, like a little sound research through musical styles or  production techniques. For this I will count on very special guests.', 1, 'active', NOW(), NOW()),
  ('Essential Mix', 'essential-mix', 'bbc-radio-one-essential-mix.jpg', null, 3, 'active', NOW(), NOW());

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

INSERT INTO `tracklist` (`name`, `slug`, `performed`, `image`, `user_id`, `status`, `created`, `updated`)
VALUES
  ('Sasha & John Digweed @ Carl Cox & Friends, Megastructure Stage, Ultra Music Festival Miami, MMW, United States', 'umf', '2017-03-25', 'artwork-umf-miami.jpg', 1, 'active', NOW(), NOW()),
  ('Transitions 657', 'transitions-657', '2017-03-31', null, 1, 'active', NOW(), NOW()),
  ('Transitions 658', 'transitions-658', '2017-04-07', null, 1, 'active', NOW(), NOW()),
  ('Transitions 659', 'transitions-659', '2017-04-14', null, 1, 'active', NOW(), NOW()),
  ('Transitions 660', 'transitions-660', '2017-04-21', null, 1, 'active', NOW(), NOW()),
  ('Transitions 661', 'transitions-661', '2017-04-28', null, 1, 'active', NOW(), NOW()),
  ('Transitions 662', 'transitions-662', '2017-05-05', null, 1, 'active', NOW(), NOW()),
  ('Transitions 663', 'transitions-663', '2017-05-12', null, 1, 'active', NOW(), NOW()),
  ('Transitions 664', 'transitions-664', '2017-05-19', null, 1, 'active', NOW(), NOW()),
  ('Transitions 665', 'transitions-665', '2017-05-26', null, 1, 'active', NOW(), NOW()),
  ('Transitions 666', 'transitions-666', '2017-06-02', null, 1, 'active', NOW(), NOW()),
  ('Transitions 667', 'transitions-667', '2017-06-09', null, 1, 'active', NOW(), NOW()),
  ('Solomun @ Space Closing Fiesta, Terraza, Space Ibiza, Spain (Be-At TV)', 'space-closing-fiesta', '2016-10-02', null, 3, 'active', NOW(), NOW()),
  ('Adam Beyer @ Drumcode 354 (Metro City Perth, Australia 2017-04-24)', 'drumcode-354', '2017-05-18', null, 1, 'active', NOW(), NOW()),
  ('Sasha @ Last Night On Earth 025 (Coachella Festival, United States)', 'last-night-on-earth-025', '2017-05-29', null, 2, 'active', NOW(), NOW());

INSERT INTO `media` (`url`, `type`, `tracklist_id`, `artist_id`, `label_id`, `user_id`, `status`, `created`, `updated`)
VALUES
  ('https://youtu.be/tI7ywh2sI04', 'youtube', 1, null, null, 1, 'active', NOW(), NOW()),
  ('https://www.mixcloud.com/globaldjmix/john-digweed-jesper-dahlback-transitions-664-2017-05-19/', 'mixcloud', 1, null, null, 2, 'active', NOW(), NOW()),
  ('https://soundcloud.com/john-digweed', 'soundcloud', null, 2, null, 3, 'active', NOW(), NOW()),
  ('https://www.youtube.com/channel/UCXUO2biGVP7FKCqPEDwmt4w', 'youtube', null, 2, null, 1, 'active', NOW(), NOW()),
  ('https://www.facebook.com/djjohndigweed', 'facebook', null, 2, null, 2, 'active', NOW(), NOW()),
  ('https://twitter.com/djjohndigweed', 'twitter', null, 2, null, 3, 'active', NOW(), NOW()),
  ('http://www.johndigweed.com/', 'website', null, 2, null, 1, 'active', NOW(), NOW()),
  ('https://soundcloud.com/bedrock_rec', 'soundcloud', null, null, 2, 2, 'active', NOW(), NOW());

INSERT INTO `artist_program` (artist_id, program_id)
VALUES
  (2, 1),
  (17, 2),
  (18, 3),
  (19, 4),
  (20, 5);

INSERT INTO `track_artist` (`track_id`, `artist_id`)
VALUES
  (1, 3),
  (2, 3),
  (3, 5),
  (4, 6),
  (5, 8),
  (6, 9),
  (7, 11),
  (8, 12),
  (9, 13),
  (10, 14);

INSERT INTO `track_remixer` (`track_id`, `artist_id`)
VALUES
  (2, 4),
  (4, 7),
  (6, 9),
  (6, 10),
  (10, 15);

INSERT INTO tracklist_genre (tracklist_id, genre_id)
VALUES
  (1, 2),
  (1, 3),
  (2, 5),
  (2, 4),
  (3, 5),
  (3, 4),
  (4, 5),
  (4, 4),
  (5, 5),
  (5, 4),
  (6, 5),
  (6, 6),
  (7, 4),
  (7, 2),
  (8, 5),
  (8, 2),
  (9, 5),
  (9, 2),
  (10, 5),
  (10, 4),
  (11, 5),
  (11, 3),
  (12, 3),
  (12, 4),
  (13, 5),
  (13, 4),
  (14, 1),
  (14, 4),
  (15, 5),
  (15, 4),
  (15, 3);

INSERT INTO `tracklist_artist` (`tracklist_id`, `artist_id`)
VALUES
  (1, 1),
  (1, 2),
  (2, 2),
  (2, 23),
  (3, 2),
  (3, 24),
  (4, 2),
  (4, 25),
  (5, 2),
  (5, 26),
  (6, 2),
  (6, 27),
  (7, 2),
  (7, 28),
  (8, 2),
  (8, 29),
  (9, 2),
  (9, 16),
  (10, 2),
  (10, 30),
  (11, 2),
  (11, 31),
  (12, 2),
  (12, 32),
  (13, 18),
  (14, 21),
  (15, 1);

INSERT INTO `track_tracklist` (track_id, tracklist_id)
VALUES
  (1, 1),
  (2, 1),
  (3, 1),
  (4, 1),
  (5, 1),
  (6, 1),
  (7, 1),
  (8, 1),
  (9, 1),
  (10, 1);
