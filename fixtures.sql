INSERT INTO `user` (`email`, `password`, `salt`, `display_name`, `status`, `created`) VALUES ('joe@example.com', 'password', 'salt', 'joey', 'active', NOW());
INSERT INTO `user` (`email`, `password`, `salt`, `display_name`, `status`, `created`) VALUES ('john@example.com', 'password', 'salt', 'john', 'active', NOW());
INSERT INTO `user` (`email`, `password`, `salt`, `display_name`, `status`, `created`) VALUES ('pete@example.com', 'password', 'salt', 'pete', 'active', NOW());

INSERT INTO `artist` (`name`, `created`) VALUES ('Sasha', NOW());
INSERT INTO `artist` (`name`, `created`) VALUES ('John Digweed', NOW());

INSERT INTO `track` (`name`, `created`) VALUES ('Xpander', NOW());
INSERT INTO `track` (`name`, `created`) VALUES ('Coma', NOW());
INSERT INTO `track` (`name`, `created`) VALUES ('Who Killed Sparky', NOW());
INSERT INTO `track` (`name`, `created`) VALUES ('Beautiful Strange', NOW());
INSERT INTO `track` (`name`, `created`) VALUES ('Gridlock', NOW());

INSERT INTO `track_artists` (`track_id`, `artist_id`) VALUES (1, 1);
INSERT INTO `track_artists` (`track_id`, `artist_id`) VALUES (2, 1);
INSERT INTO `track_artists` (`track_id`, `artist_id`) VALUES (3, 1);
INSERT INTO `track_artists` (`track_id`, `artist_id`) VALUES (4, 2);
INSERT INTO `track_artists` (`track_id`, `artist_id`) VALUES (5, 2);

