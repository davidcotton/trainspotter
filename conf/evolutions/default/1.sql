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

create table genre_tracklist (
  genre_id                      bigint not null,
  tracklist_id                  bigint not null,
  constraint pk_genre_tracklist primary key (genre_id,tracklist_id)
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

create table track (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  remix_name                    varchar(255),
  genre_id                      bigint,
  release_date                  date,
  created                       datetime not null,
  updated                       datetime not null,
  constraint pk_track primary key (id)
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
  email                         varchar(191),
  password                      varchar(255) not null,
  salt                          varchar(255) not null,
  display_name                  varchar(191) not null,
  status                        varchar(10) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint ck_user_status check (status in ('inactive','deleted','unverified','active','banned')),
  constraint uq_user_email unique (email),
  constraint uq_user_display_name unique (display_name),
  constraint pk_user primary key (id)
);

alter table genre_tracklist add constraint fk_genre_tracklist_genre foreign key (genre_id) references genre (id) on delete restrict on update restrict;
create index ix_genre_tracklist_genre on genre_tracklist (genre_id);

alter table genre_tracklist add constraint fk_genre_tracklist_tracklist foreign key (tracklist_id) references tracklist (id) on delete restrict on update restrict;
create index ix_genre_tracklist_tracklist on genre_tracklist (tracklist_id);

alter table media add constraint fk_media_tracklist_id foreign key (tracklist_id) references tracklist (id) on delete restrict on update restrict;
create index ix_media_tracklist_id on media (tracklist_id);

alter table media add constraint fk_media_artist_id foreign key (artist_id) references artist (id) on delete restrict on update restrict;
create index ix_media_artist_id on media (artist_id);

alter table media add constraint fk_media_label_id foreign key (label_id) references label (id) on delete restrict on update restrict;
create index ix_media_label_id on media (label_id);

alter table track add constraint fk_track_genre_id foreign key (genre_id) references genre (id) on delete restrict on update restrict;
create index ix_track_genre_id on track (genre_id);

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

alter table genre_tracklist drop foreign key fk_genre_tracklist_genre;
drop index ix_genre_tracklist_genre on genre_tracklist;

alter table genre_tracklist drop foreign key fk_genre_tracklist_tracklist;
drop index ix_genre_tracklist_tracklist on genre_tracklist;

alter table media drop foreign key fk_media_tracklist_id;
drop index ix_media_tracklist_id on media;

alter table media drop foreign key fk_media_artist_id;
drop index ix_media_artist_id on media;

alter table media drop foreign key fk_media_label_id;
drop index ix_media_label_id on media;

alter table track drop foreign key fk_track_genre_id;
drop index ix_track_genre_id on track;

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

drop table if exists genre_tracklist;

drop table if exists label;

drop table if exists media;

drop table if exists track;

drop table if exists tracklist;

drop table if exists tracklist_artist;

drop table if exists tracklist_genre;

drop table if exists user;

