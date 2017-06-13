# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table artist (
  id                            bigint auto_increment not null,
  name                          varchar(191) not null,
  image                         varchar(255),
  created                       datetime not null,
  updated                       datetime not null,
  constraint uq_artist_name unique (name),
  constraint pk_artist primary key (id)
);

create table genre (
  id                            bigint auto_increment not null,
  name                          varchar(191) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint uq_genre_name unique (name),
  constraint pk_genre primary key (id)
);

create table label (
  id                            bigint auto_increment not null,
  name                          varchar(191) not null,
  image                         varchar(255),
  created                       datetime not null,
  updated                       datetime not null,
  constraint uq_label_name unique (name),
  constraint pk_label primary key (id)
);

create table media (
  id                            bigint auto_increment not null,
  url                           varchar(191) not null,
  tracklist_id                  bigint,
  artist_id                     bigint,
  label_id                      bigint,
  created                       datetime not null,
  updated                       datetime not null,
  constraint uq_media_url unique (url),
  constraint pk_media primary key (id)
);

create table token (
  id                            bigint auto_increment not null,
  user_id                       bigint not null,
  signature                     varbinary(255) not null,
  expiry                        datetime not null,
  constraint uq_token_user_id unique (user_id),
  constraint pk_token primary key (id)
);

create table track (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  remix_name                    varchar(255),
  genre_id                      bigint,
  label_id                      bigint,
  release_date                  date,
  created                       datetime not null,
  updated                       datetime not null,
  constraint pk_track primary key (id)
);

create table track_artists (
  track_id                      bigint not null,
  artist_id                     bigint not null,
  constraint pk_track_artists primary key (track_id,artist_id)
);

create table track_remixers (
  track_id                      bigint not null,
  artist_id                     bigint not null,
  constraint pk_track_remixers primary key (track_id,artist_id)
);

create table track_tracklist (
  track_id                      bigint not null,
  tracklist_id                  bigint not null,
  constraint pk_track_tracklist primary key (track_id,tracklist_id)
);

create table tracklist (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  date                          date,
  user_id                       bigint not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint pk_tracklist primary key (id)
);

create table tracklist_artist (
  tracklist_id                  bigint not null,
  artist_id                     bigint not null,
  constraint pk_tracklist_artist primary key (tracklist_id,artist_id)
);

create table tracklist_genre (
  tracklist_id                  bigint not null,
  genre_id                      bigint not null,
  constraint pk_tracklist_genre primary key (tracklist_id,genre_id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(191) not null,
  display_name                  varchar(191) not null,
  status                        varchar(10) not null,
  hash                          char(60) not null,
  salt                          char(29) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint ck_user_status check (status in ('inactive','deleted','unverified','active','banned')),
  constraint uq_user_email unique (email),
  constraint uq_user_display_name unique (display_name),
  constraint pk_user primary key (id)
);

alter table media add constraint fk_media_tracklist_id foreign key (tracklist_id) references tracklist (id) on delete restrict on update restrict;
create index ix_media_tracklist_id on media (tracklist_id);

alter table media add constraint fk_media_artist_id foreign key (artist_id) references artist (id) on delete restrict on update restrict;
create index ix_media_artist_id on media (artist_id);

alter table media add constraint fk_media_label_id foreign key (label_id) references label (id) on delete restrict on update restrict;
create index ix_media_label_id on media (label_id);

alter table token add constraint fk_token_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table track add constraint fk_track_genre_id foreign key (genre_id) references genre (id) on delete restrict on update restrict;
create index ix_track_genre_id on track (genre_id);

alter table track add constraint fk_track_label_id foreign key (label_id) references label (id) on delete restrict on update restrict;
create index ix_track_label_id on track (label_id);

alter table track_artists add constraint fk_track_artists_track foreign key (track_id) references track (id) on delete restrict on update restrict;
create index ix_track_artists_track on track_artists (track_id);

alter table track_artists add constraint fk_track_artists_artist foreign key (artist_id) references artist (id) on delete restrict on update restrict;
create index ix_track_artists_artist on track_artists (artist_id);

alter table track_remixers add constraint fk_track_remixers_track foreign key (track_id) references track (id) on delete restrict on update restrict;
create index ix_track_remixers_track on track_remixers (track_id);

alter table track_remixers add constraint fk_track_remixers_artist foreign key (artist_id) references artist (id) on delete restrict on update restrict;
create index ix_track_remixers_artist on track_remixers (artist_id);

alter table track_tracklist add constraint fk_track_tracklist_track foreign key (track_id) references track (id) on delete restrict on update restrict;
create index ix_track_tracklist_track on track_tracklist (track_id);

alter table track_tracklist add constraint fk_track_tracklist_tracklist foreign key (tracklist_id) references tracklist (id) on delete restrict on update restrict;
create index ix_track_tracklist_tracklist on track_tracklist (tracklist_id);

alter table tracklist add constraint fk_tracklist_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_tracklist_user_id on tracklist (user_id);

alter table tracklist_artist add constraint fk_tracklist_artist_tracklist foreign key (tracklist_id) references tracklist (id) on delete restrict on update restrict;
create index ix_tracklist_artist_tracklist on tracklist_artist (tracklist_id);

alter table tracklist_artist add constraint fk_tracklist_artist_artist foreign key (artist_id) references artist (id) on delete restrict on update restrict;
create index ix_tracklist_artist_artist on tracklist_artist (artist_id);

alter table tracklist_genre add constraint fk_tracklist_genre_tracklist foreign key (tracklist_id) references tracklist (id) on delete restrict on update restrict;
create index ix_tracklist_genre_tracklist on tracklist_genre (tracklist_id);

alter table tracklist_genre add constraint fk_tracklist_genre_genre foreign key (genre_id) references genre (id) on delete restrict on update restrict;
create index ix_tracklist_genre_genre on tracklist_genre (genre_id);


# --- !Downs

alter table media drop foreign key fk_media_tracklist_id;
drop index ix_media_tracklist_id on media;

alter table media drop foreign key fk_media_artist_id;
drop index ix_media_artist_id on media;

alter table media drop foreign key fk_media_label_id;
drop index ix_media_label_id on media;

alter table token drop foreign key fk_token_user_id;

alter table track drop foreign key fk_track_genre_id;
drop index ix_track_genre_id on track;

alter table track drop foreign key fk_track_label_id;
drop index ix_track_label_id on track;

alter table track_artists drop foreign key fk_track_artists_track;
drop index ix_track_artists_track on track_artists;

alter table track_artists drop foreign key fk_track_artists_artist;
drop index ix_track_artists_artist on track_artists;

alter table track_remixers drop foreign key fk_track_remixers_track;
drop index ix_track_remixers_track on track_remixers;

alter table track_remixers drop foreign key fk_track_remixers_artist;
drop index ix_track_remixers_artist on track_remixers;

alter table track_tracklist drop foreign key fk_track_tracklist_track;
drop index ix_track_tracklist_track on track_tracklist;

alter table track_tracklist drop foreign key fk_track_tracklist_tracklist;
drop index ix_track_tracklist_tracklist on track_tracklist;

alter table tracklist drop foreign key fk_tracklist_user_id;
drop index ix_tracklist_user_id on tracklist;

alter table tracklist_artist drop foreign key fk_tracklist_artist_tracklist;
drop index ix_tracklist_artist_tracklist on tracklist_artist;

alter table tracklist_artist drop foreign key fk_tracklist_artist_artist;
drop index ix_tracklist_artist_artist on tracklist_artist;

alter table tracklist_genre drop foreign key fk_tracklist_genre_tracklist;
drop index ix_tracklist_genre_tracklist on tracklist_genre;

alter table tracklist_genre drop foreign key fk_tracklist_genre_genre;
drop index ix_tracklist_genre_genre on tracklist_genre;

drop table if exists artist;

drop table if exists genre;

drop table if exists label;

drop table if exists media;

drop table if exists token;

drop table if exists track;

drop table if exists track_artists;

drop table if exists track_remixers;

drop table if exists track_tracklist;

drop table if exists tracklist;

drop table if exists tracklist_artist;

drop table if exists tracklist_genre;

drop table if exists user;

