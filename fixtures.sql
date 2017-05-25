INSERT INTO `user`
(`email`, `password`, `salt`, `display_name`, `status`, `created`, `updated`)
VALUES
  ('joe@example.com', 'password', 'salt', 'joey', 'active', NOW(), NOW()),
  ('john@example.com', 'password', 'salt', 'john', 'active', NOW(), NOW()),
  ('pete@example.com', 'password', 'salt', 'pete', 'active', NOW(), NOW())
;

INSERT INTO `artist`
(`name`, `created`, `updated`)
VALUES
  ('Sasha', NOW(), NOW()),
  ('John Digweed', NOW(), NOW()),
  ('Customer', NOW(), NOW()),
  ('Deetron', NOW(), NOW()),
  ('Kaled', NOW(), NOW()),
  ('Lessons', NOW(), NOW()),
  ('Adam Port', NOW(), NOW()),
  ('Re.You', NOW(), NOW()),
  ('Tiefschwarz', NOW(), NOW()),
  ('Yawk', NOW(), NOW()),
  ('Soame', NOW(), NOW()),
  ('Wigbert', NOW(), NOW()),
  ('Citizen', NOW(), NOW()),
  ('Kocleo', NOW(), NOW()),
  ('Scan Mode', NOW(), NOW()),
  ('Jesper Dahlback', NOW(), NOW())
;

INSERT INTO `label`
(`name`, `created`, `updated`)
VALUES
  ('Not on label', NOW(), NOW()),
  ('Bedrock', NOW(), NOW()),
  ('Get Wet', NOW(), NOW()),
  ('Sinnbus', NOW(), NOW()),
  ('Mobilee', NOW(), NOW()),
  ('Souvenir', NOW(), NOW()),
  ('Colour Series', NOW(), NOW()),
  ('SCI+TEC', NOW(), NOW()),
  ('IAM Music', NOW(), NOW()),
  ('Strange Idol', NOW(), NOW())
;

INSERT INTO `genre`
(name, created, updated)
VALUES
  ('House', NOW(), NOW()),
  ('Techno', NOW(), NOW()),
  ('Deep House', NOW(), NOW()),
  ('Tech House', NOW(), NOW()),
  ('Progressive', NOW(), NOW())
;


INSERT INTO `track`
(`name`, `remix_name`, `genre_id`, `label_id`, `release_date`, `created`, `updated`)
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
  ('Keep Faith', 'Scan Mode Remix', 2, 9, NOW(), NOW(), NOW())
;

INSERT INTO `track_artists`
(`track_id`, `artist_id`)
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
  (10, 14)
;

INSERT INTO `track_remixers`
(`track_id`, `artist_id`)
VALUES
  (2, 4),
  (4, 7),
  (6, 9),
  (6, 10),
  (10, 15)
;

INSERT INTO `tracklist`
(`name`, `media_url`, `date`, `user_id`, `created`, `updated`)
VALUES
  ('John Digweed & Jesper Dahlback - Transitions 664 2017-05-19', null, '2017-05-19', 1, NOW(), NOW())
;

INSERT INTO `tracklist_artist`
(`tracklist_id`, `artist_id`)
VALUES
  (1, 2),
  (1, 16)
;

INSERT INTO `track_tracklist`
(track_id, tracklist_id)
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
  (10, 1)
;
