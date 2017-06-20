
INSERT INTO `user` (`email`, `hash`, `salt`, `display_name`, `status`, `created`, `updated`)
VALUES
  ('brian.mcgee@simpsons.com', '$2a$16$T1PaqXFutgw9qUmlK875Ge4wFRnn9TBMyJHfxyBpDXItcrNDL/OYa', '$2a$16$T1PaqXFutgw9qUmlK875Ge', 'Brian McGee', 'active', NOW(), NOW()),
  ('rembrandt.q.einstein@simpsons.com', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.xKIJUcj9lInDBANSKNmiS5WCKW7uvai', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.', 'Rembrandt Q. Einstein', 'active', NOW(), NOW()),
  ('rory.b.bellows@simpsons.com', '$2a$16$aY..e8GAU2YGfdvLGqtaheWo5I7vwq9SPc7bqX8hgbgdSQEVUYGSq', '$2a$16$aY..e8GAU2YGfdvLGqtahe', 'Rory B. Bellows', 'active', NOW(), NOW());

INSERT INTO `artist` (`name`, `route`, `image`, `description`, `created`, `updated`)
VALUES
  ('Sasha', 'sasha', 'sasha.jpg', null , NOW(), NOW()),
  ('John Digweed', 'john-digweed', 'john-digweed.jpg', null , NOW(), NOW()),
  ('Customer', 'customer', null, null , NOW(), NOW()),
  ('Deetron', 'deetron', 'deetron.jpg', null , NOW(), NOW()),
  ('Kaled', 'kaled', null, null , NOW(), NOW()),
  ('Lessons', 'lessons', null, null , NOW(), NOW()),
  ('Adam Port', 'adam-port', 'adam-port.jpg', null , NOW(), NOW()),
  ('Re.You', 're.you', 're.you.jpg', null , NOW(), NOW()),
  ('Tiefschwarz', 'tiefschwarz', 'tiefschwarz.jpg', null , NOW(), NOW()),
  ('Yawk', 'yawk', null, null , NOW(), NOW()),
  ('Soame', 'soame', null, null , NOW(), NOW()),
  ('Wigbert', 'wigbert', null, null, NOW(), NOW()),
  ('Citizen', 'citizen', 'citizen.jpg', null , NOW(), NOW()),
  ('Kocleo', 'kocleo', null, null , NOW(), NOW()),
  ('Scan Mode', 'scan-mode', null, null , NOW(), NOW()),
  ('Jesper Dahlback', 'jesper-dahlback', 'jesper-dahlback.jpg', null , NOW(), NOW()),
  ('Umek', 'umek', 'umek.jpg', 'For a long time, Slovenia is known for its great electronic parties, but as the local scene is of boutique dimensions, everyone, who wishes to take the art of mixing and producing to a professional level, has to go out there and enter the worldwide scene. Umek has done this step as early as in the middle of the 90s and so he became one of the most important techno players of the global techno scene, even before the end of the last millennium. Together with people, who shared the same views, he successfully captured the first era of researching and recreating the rules of the dance music in Recycled Loops, Consumer Recreation and Astrodisco platforms, which all put the sound of the Slovenian techno on the world map of electronic music.' , NOW(), NOW()),
  ('Solomun', 'solomun', 'solomun.jpg', null , NOW(), NOW()),
  ('Marc Romboy', 'marc-romboy', 'marc-romboy.jpg', null , NOW(), NOW()),
  ('Henry Saiz', 'henry-saiz', 'henry-saiz.jpg', null , NOW(), NOW()),
  ('Adam Beyer', 'adam-beyer', 'adam-beyer.jpg', null , NOW(), NOW()),
  ('Carl Cox', 'carl-cox', 'carl-cox.jpg', null , NOW(), NOW()),
  ('Joeski', 'joeski', 'joeski.jpg', null , NOW(), NOW()),
  ('Quivver', 'quivver', null, null , NOW(), NOW()),
  ('Jamie Anderson', 'jamie-anderson', 'jamie-anderson.jpg', null , NOW(), NOW()),
  ('Jimmy Van M', 'jimmy-van-m', 'jimmy-van-m.jpg', null , NOW(), NOW()),
  ('Undo', 'undo', null, null , NOW(), NOW()),
  ('Harvey McKay', 'harvey-mckay', 'harvey-mckay.jpg', null , NOW(), NOW()),
  ('Dance Spirit', 'dance-spirit', 'dance-spirit.jpg', null , NOW(), NOW()),
  ('DJ Three', 'dj-three', null, null , NOW(), NOW()),
  ('Jeremy Olander', 'jeremy-olander', 'jeremy-olander.jpg', null , NOW(), NOW()),
  ('Öona Dahl', 'oona-dahl', null, null , NOW(), NOW()),
  ('Pete Tong', 'pete-tong', 'pete-tong.jpg', null , NOW(), NOW());

INSERT INTO `channel` (`name`, `route`, `image`, `description`, `created`, `updated`) VALUES
  ('Proton Radio', 'proton-radio', 'proton-opengraph.gif','Welcome to Proton, a subscriber supported Internet radio station and music label.' , NOW(), NOW()),
  ('Triple J', 'triple-j', 'triple-j.jpg', 'triple j is the national youth broadcaster for young Australians. We aim to bring you the latest, greatest music and the stories that matter on the radio (covering over 98% of the nation), online, on your phone and in print with J Annual.' , NOW(), NOW()),
  ('BBC Radio 1', 'bbc-radio-1', 'bbc.jpg', 'Listen to BBC Radio 1, home of the Official Chart, the Live Lounge and the world''s greatest DJs including Nick Grimshaw, Scott Mills, Clara Amfo, Greg James, Annie Mac and many more. Listen on DAB Radio, Digital TV, 97-99 FM, Online and Mobile.' , NOW(), NOW()),
  ('KISS FM', 'kiss-fm', 'kiss-fm.jpg', 'Kiss is a UK radio station which broadcasts on FM and National DAB and specialises in pop, hip hop, R&B, urban and electronic dance music.' , NOW(), NOW()),
  ('Energia 97 FM', 'energia-97-fm', null, '' , NOW(), NOW());

INSERT INTO `label` (`name`, `route`, `image`, `description`, `created`, `updated`)
VALUES
  ('Not on label', 'not-on-label', null,'' , NOW(), NOW()),
  ('Bedrock Records', 'bedrock-records', 'bedrock-records.jpg', 'Together John Digweed and Nick Muir are the key to what makes up the Bedrock record label tick. With the some of the cream of the progressive and breaks crop releases music on Bedrock the label constantly goes from strength to strength and provides the latest in quality beats. \nBedrock launched in 1999 with the classic progressive anthem Heaven Scent.' , NOW(), NOW()),
  ('Get Wet', 'get-wet', null, '' ,  NOW(), NOW()),
  ('Sinnbus', 'sinnbus', 'sinnbus.jpg', '' , NOW(), NOW()),
  ('Mobilee', 'mobilee', 'mobilee.jpg', '' , NOW(), NOW()),
  ('Souvenir', 'souvenir', 'souvenir.jpg', '' , NOW(), NOW()),
  ('Colour Series', 'colour-series', null, '' , NOW(), NOW()),
  ('SCI+TEC', 'sci+tec', 'sci+tec.jpg', '' , NOW(), NOW()),
  ('IAM Music', 'iam-music', 'iam-music.jpg', '' , NOW(), NOW()),
  ('Strange Idol', 'strange-idol', 'strange-idol.jpg', '' , NOW(), NOW()),
  ('Last Night On Earth', 'last-night-on-earth', null, 'Label run by Sasha' , NOW(), NOW()),
  ('Drumcode', 'drumcode', 'drumcode.jpg', 'Techno label run by Adam Beyer.' , NOW(), NOW());

INSERT INTO `genre` (`name`, `route`, `created`, `updated`)
VALUES
  ('House', 'house', NOW(), NOW()),
  ('Techno', 'techno', NOW(), NOW()),
  ('Deep House', 'deep-house', NOW(), NOW()),
  ('Tech House', 'tech-house', NOW(), NOW()),
  ('Progressive House', 'progressive-house', NOW(), NOW());

INSERT INTO `program` (`name`, `route`, `image`, `description`, `channel_id`, `created`, `updated`) VALUES
  ('Transitions', 'transitions', 'transitions.jpg', 'The most important radio show in house music needs no introduction, but here it goes anyway. John Digweed\'s long-standing weekly 2 hour program is legendary, airing for many years exclusively on Kiss 100 in London but now syndicated around the world and internet. In 2008, the show comes to Proton Radio. Expect upfront & unreleased tunes dished up by John Digweed alongside finely tuned guest mixes that are almost always exclusively prepared just for the show.', 1, NOW(), NOW()),
  ('Behind The Iron Curtain', 'behind-the-iron-curtain', 'behind-the-iron-curtain.jpg', 'Welcome to UMEK’s hour-long trip behind the iron curtain. Hear it here on KISS FM every Tuesday 5 – 6 am. It would take a long essay to fully explain UMEK’s meaning to electronic music. The Slovenian born producer and DJ has been setting trends and rocking dance floors for two decades now — and still shows no signs of slowing down. He’s tireless in his techno and tech house production and with more than 100 gigs per year, probably one of the busiest techno DJs out there.', 1, NOW(), NOW()),
  ('Diynamic', 'diynamic', 'diynamic.jpg', 'The weekly Diynamic radio show is hosted by Solomun & friends.', 1, NOW(), NOW()),
  ('Systematic Session', 'systematic-session', 'systematic.jpg', 'Systematic Recordings from Germany has now finally its own exclusive radio show. The label which is run by the worldwide well known DJ/ producer Marc Romboy has released dozens of trendsetting and forwardthinking electronic house tracks, contributed by artists like Stephan Bodzin, Steve Lawler, Booka Shade, Chelonis R. Jones, John Dahlback and Robert Babicz, just to name a few. The show is called "Systematic Sessions" and serves you one hour of Dj mixed up-to-date records by their host Marc Romboy and other globally profiled DJs and producers. You want to know more about Marc Romboy and Systematic recordings? ', 1, NOW(), NOW()),
  ('The Labyrinth', 'the-labyrinth', 'henry-saiz.jpg', 'Hi everybody, this is Henry Saiz, welcome to the labyrinth, my first online radio show. I would like to use this platform to share with you some ideas about music, I will try to develop a concept in every show, like a little sound research through musical styles or  production techniques. For this I will count on very special guests.', 1, NOW(), NOW()),
  ('Essential Mix', 'essential-mix', 'bbc-radio-one-essential-mix.jpg', '', 3, NOW(), NOW());

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

INSERT INTO `tracklist` (`name`, `route`, `performed`, `image`, `user_id`, `created`, `updated`)
VALUES
  ('Sasha & John Digweed @ Carl Cox & Friends, Megastructure Stage, Ultra Music Festival Miami, MMW, United States', '', '2017-03-25', 'artwork-umf-miami.jpg', 1, NOW(), NOW()),
  ('Transitions 657', 'transitions-657', '2017-03-31', null, 1, NOW(), NOW()),
  ('Transitions 658', 'transitions-658', '2017-04-07', null, 1, NOW(), NOW()),
  ('Transitions 659', 'transitions-659', '2017-04-14', null, 1, NOW(), NOW()),
  ('Transitions 660', 'transitions-660', '2017-04-21', null, 1, NOW(), NOW()),
  ('Transitions 661', 'transitions-661', '2017-04-28', null, 1, NOW(), NOW()),
  ('Transitions 662', 'transitions-662', '2017-05-05', null, 1, NOW(), NOW()),
  ('Transitions 663', 'transitions-663', '2017-05-12', null, 1, NOW(), NOW()),
  ('Transitions 664', 'transitions-664', '2017-05-19', null, 1, NOW(), NOW()),
  ('Transitions 665', 'transitions-665', '2017-05-26', null, 1, NOW(), NOW()),
  ('Transitions 666', 'transitions-666', '2017-06-02', null, 1, NOW(), NOW()),
  ('Transitions 667', 'transitions-667', '2017-06-09', null, 1, NOW(), NOW()),
  ('Solomun @ Space Closing Fiesta, Terraza, Space Ibiza, Spain (Be-At TV)', 'space-closing-fiesta', '2016-10-02', null, 3, NOW(), NOW()),
  ('Adam Beyer @ Drumcode 354 (Metro City Perth, Australia 2017-04-24)', 'drumcode-354', '2017-05-18', null, 1, NOW(), NOW()),
  ('Sasha @ Last Night On Earth 025 (Coachella Festival, United States)', 'last-night-on-earth-025', '2017-05-29', null, 2, NOW(), NOW());

INSERT INTO `media` (url, tracklist_id, artist_id, label_id, created, updated)
VALUES
  ('https://youtu.be/tI7ywh2sI04', 1, null, null, NOW(), NOW()),
  ('https://www.mixcloud.com/globaldjmix/john-digweed-jesper-dahlback-transitions-664-2017-05-19/', 1, null, null, NOW(), NOW()),
  ('https://soundcloud.com/john-digweed', null, 2, null, NOW(), NOW()),
  ('https://www.youtube.com/channel/UCXUO2biGVP7FKCqPEDwmt4w', null, 2, null, NOW(), NOW()),
  ('https://www.facebook.com/djjohndigweed', null, 2, null, NOW(), NOW()),
  ('https://twitter.com/djjohndigweed', null, 2, null, NOW(), NOW()),
  ('http://www.johndigweed.com/', null, 2, null, NOW(), NOW()),
  ('https://soundcloud.com/bedrock_rec', null, null, 2, NOW(), NOW());

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
  (1, 3);

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
