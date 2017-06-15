
INSERT INTO `user` (`email`, `hash`, `salt`, `display_name`, `status`, `created`, `updated`)
VALUES
  ('brian.mcgee@simpsons.com', '$2a$16$T1PaqXFutgw9qUmlK875Ge4wFRnn9TBMyJHfxyBpDXItcrNDL/OYa', '$2a$16$T1PaqXFutgw9qUmlK875Ge', 'Brian McGee', 'active', NOW(), NOW()),
  ('rembrandt.q.einstein@simpsons.com', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.xKIJUcj9lInDBANSKNmiS5WCKW7uvai', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.', 'Rembrandt Q. Einstein', 'active', NOW(), NOW()),
  ('rory.b.bellows@simpsons.com', '$2a$16$aY..e8GAU2YGfdvLGqtaheWo5I7vwq9SPc7bqX8hgbgdSQEVUYGSq', '$2a$16$aY..e8GAU2YGfdvLGqtahe', 'Rory B. Bellows', 'active', NOW(), NOW());

INSERT INTO `artist` (`name`, `image`, `description`, `created`, `updated`)
VALUES
  ('Sasha', 'sasha.jpg','' , NOW(), NOW()),
  ('John Digweed', 'john-digweed.jpg','' , NOW(), NOW()),
  ('Customer', null,'' , NOW(), NOW()),
  ('Deetron', 'deetron.jpg','' , NOW(), NOW()),
  ('Kaled', null,'' , NOW(), NOW()),
  ('Lessons', null,'' , NOW(), NOW()),
  ('Adam Port', 'adam-port.jpg','' , NOW(), NOW()),
  ('Re.You', 're.you.jpg','' , NOW(), NOW()),
  ('Tiefschwarz', 'tiefschwarz.jpg','' , NOW(), NOW()),
  ('Yawk', null,'' , NOW(), NOW()),
  ('Soame', null,'' , NOW(), NOW()),
  ('Wigbert', null, '', NOW(), NOW()),
  ('Citizen', 'citizen.jpg','' , NOW(), NOW()),
  ('Kocleo', null,'' , NOW(), NOW()),
  ('Scan Mode', null,'' , NOW(), NOW()),
  ('Jesper Dahlback', 'jesper-dahlback.jpg','' , NOW(), NOW()),
  ('Umek', 'umek.jpg','' , NOW(), NOW()),
  ('Solomun', 'solomun.jpg','' , NOW(), NOW()),
  ('Marc Romboy', 'marc_romboy.jpg','' , NOW(), NOW()),
  ('Henry Saiz', 'henry-saiz.jpg','' , NOW(), NOW()),
  ('Adam Beyer', 'adam-beyer.jpg','' , NOW(), NOW());

INSERT INTO `channel` (`name`, `image`, `description`, `created`, `updated`) VALUES
  ('Proton Radio', 'proton-opengraph.gif','Welcome to Proton, a subscriber supported Internet radio station and music label.' , NOW(), NOW()),
  ('Triple J', 'triple-j.jpg', '' , NOW(), NOW()),
  ('BBC Radio 1', 'bbc.jpg', '' , NOW(), NOW()),
  ('Kiss FM', 'kiss-fm.jpg', '' , NOW(), NOW()),
  ('Energia 97 FM', null, '' , NOW(), NOW());

INSERT INTO `label` (`name`, `image`, `description`, `created`, `updated`)
VALUES
  ('Not on label', null,'' , NOW(), NOW()),
  ('Bedrock Records', 'bedrock-records.jpg', 'Together John Digweed and Nick Muir are the key to what makes up the Bedrock record label tick. With the some of the cream of the progressive and breaks crop releases music on Bedrock the label constantly goes from strength to strength and provides the latest in quality beats. \nBedrock launched in 1999 with the classic progressive anthem Heaven Scent.' , NOW(), NOW()),
  ('Get Wet', null, '' ,  NOW(), NOW()),
  ('Sinnbus', 'sinnbus.jpg', '' , NOW(), NOW()),
  ('Mobilee', 'mobilee.jpg', '' , NOW(), NOW()),
  ('Souvenir', 'souvenir.jpg', '' , NOW(), NOW()),
  ('Colour Series', null, '' , NOW(), NOW()),
  ('SCI+TEC', 'sci+tec.jpg', '' , NOW(), NOW()),
  ('IAM Music', 'iam-music.jpg', '' , NOW(), NOW()),
  ('Strange Idol', 'strange-idol.jpg', '' , NOW(), NOW()),
  ('Last Night On Earth', null, 'Label run by Sasha' , NOW(), NOW()),
  ('Drumcode', 'drumcode.jpg', 'Techno label run by Adam Beyer.' , NOW(), NOW());

INSERT INTO `genre` (name, created, updated)
VALUES
  ('House', NOW(), NOW()),
  ('Techno', NOW(), NOW()),
  ('Deep House', NOW(), NOW()),
  ('Tech House', NOW(), NOW()),
  ('Progressive', NOW(), NOW());

INSERT INTO `program` (name, image, description, channel_id, created, updated) VALUES
  ('Transitions', 'transitions.jpg', 'The most important radio show in house music needs no introduction, but here it goes anyway. John Digweed\'s long-standing weekly 2 hour program is legendary, airing for many years exclusively on Kiss 100 in London but now syndicated around the world and internet. In 2008, the show comes to Proton Radio. Expect upfront & unreleased tunes dished up by John Digweed alongside finely tuned guest mixes that are almost always exclusively prepared just for the show.', 1, NOW(), NOW()),
  ('Behind The Iron Curtain', 'behind-the-iron-curtain.jpg', null, 1, NOW(), NOW()),
  ('Diynamic', 'diynamic.jpg', 'The weekly Diynamic radio show is hosted by Solomun & friends.', 1, NOW(), NOW()),
  ('Systematic Session', 'Systematic-Session.jpg', 'Systematic Recordings from Germany has now finally its own exclusive radio show. The label which is run by the worldwide well known DJ/ producer Marc Romboy has released dozens of trendsetting and forwardthinking electronic house tracks, contributed by artists like Stephan Bodzin, Steve Lawler, Booka Shade, Chelonis R. Jones, John Dahlback and Robert Babicz, just to name a few. The show is called "Systematic Sessions" and serves you one hour of Dj mixed up-to-date records by their host Marc Romboy and other globally profiled DJs and producers. You want to know more about Marc Romboy and Systematic recordings? ', 1, NOW(), NOW()),
  ('The Labyrinth', 'The_Labyrinth.jpg', 'Hi everybody, this is Henry Saiz, welcome to the labyrinth, my first online radio show. I would like to use this platform to share with you some ideas about music, I will try to develop a concept in every show, like a little sound research through musical styles or  production techniques. For this I will count on very special guests.', 1, NOW(), NOW());

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

INSERT INTO `tracklist` (`name`, `date`, `user_id`, `created`, `updated`)
VALUES
  ('Sasha & John Digweed @ Carl Cox & Friends, Megastructure Stage, Ultra Music Festival Miami, MMW, United States', '2017-03-25', 1, NOW(), NOW()),
  ('John Digweed & Joeski - Transitions 657', '2017-03-31', 1, NOW(), NOW()),
  ('John Digweed & Quivver - Transitions 658', '2017-04-07', 1, NOW(), NOW()),
  ('John Digweed & Jamie Anderson - Transitions 659', '2017-04-14', 1, NOW(), NOW()),
  ('John Digweed & Jimmy Van M - Transitions 660', '2017-04-21', 1, NOW(), NOW()),
  ('John Digweed & Undo - Transitions 661', '2017-04-28', 1, NOW(), NOW()),
  ('John Digweed & Harvey McKay - Transitions 662', '2017-05-05', 1, NOW(), NOW()),
  ('John Digweed & Dance Spirit - Transitions 663', '2017-05-12', 1, NOW(), NOW()),
  ('John Digweed & Jesper Dahlback - Transitions 664', '2017-05-19', 1, NOW(), NOW()),
  ('John Digweed & DJ Three - Transitions 665', '2017-05-26', 1, NOW(), NOW()),
  ('John Digweed & Jeremy Olander - Transitions 666', '2017-06-02', 1, NOW(), NOW()),
  ('John Digweed & Öona Dahl - Transitions 667', '2017-06-09', 1, NOW(), NOW()),
  ('Solomun @ Space Closing Fiesta, Terraza, Space Ibiza, Spain (Be-At TV)', '2016-10-02', 3, NOW(), NOW()),
  ('Adam Beyer @ Drumcode 354 (Metro City Perth, Australia 2017-04-24)', '2017-05-18', 1, NOW(), NOW()),
  ('Sasha @ Last Night On Earth 025 (Coachella Festival, United States)', '2017-05-29', 2, NOW(), NOW());

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
  (3, 2),
  (4, 2),
  (5, 2),
  (6, 2),
  (7, 2),
  (8, 2),
  (9, 2),
  (9, 16),
  (10, 2),
  (11, 2),
  (12, 2),
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
