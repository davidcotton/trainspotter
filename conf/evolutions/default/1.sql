# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table artist (
  id                            bigint auto_increment not null,
  name                          varchar(191) not null,
  slug                          varchar(191) not null,
  image                         varchar(255),
  description                   text,
  status                        varchar(7) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint ck_artist_status check (status in ('deleted','active')),
  constraint uq_artist_name unique (name),
  constraint uq_artist_slug unique (slug),
  constraint pk_artist primary key (id)
);

create table artist_program (
  artist_id                     bigint not null,
  program_id                    bigint not null,
  constraint pk_artist_program primary key (artist_id,program_id)
);

create table channel (
  id                            bigint auto_increment not null,
  name                          varchar(191) not null,
  slug                          varchar(191) not null,
  image                         varchar(255),
  description                   text,
  status                        varchar(7) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint ck_channel_status check (status in ('deleted','active')),
  constraint uq_channel_name unique (name),
  constraint uq_channel_slug unique (slug),
  constraint pk_channel primary key (id)
);

create table genre (
  id                            bigint auto_increment not null,
  name                          varchar(191) not null,
  slug                          varchar(191) not null,
  status                        varchar(7) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint ck_genre_status check (status in ('deleted','active')),
  constraint uq_genre_name unique (name),
  constraint uq_genre_slug unique (slug),
  constraint pk_genre primary key (id)
);

create table label (
  id                            bigint auto_increment not null,
  name                          varchar(191) not null,
  slug                          varchar(191) not null,
  image                         varchar(255),
  description                   text,
  status                        varchar(7) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint ck_label_status check (status in ('deleted','active')),
  constraint uq_label_name unique (name),
  constraint uq_label_slug unique (slug),
  constraint pk_label primary key (id)
);

create table media (
  id                            bigint auto_increment not null,
  url                           varchar(191) not null,
  type                          varchar(10) not null,
  tracklist_id                  bigint,
  artist_id                     bigint,
  label_id                      bigint,
  user_id                       bigint,
  status                        varchar(7) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint ck_media_type check (type in ('mixcloud','youtube','twitter','website','facebook','soundcloud')),
  constraint ck_media_status check (status in ('deleted','pending','active')),
  constraint uq_media_url unique (url),
  constraint pk_media primary key (id)
);

create table program (
  id                            bigint auto_increment not null,
  name                          varchar(191) not null,
  slug                          varchar(191) not null,
  image                         varchar(255),
  description                   text,
  channel_id                    bigint,
  status                        varchar(7) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint ck_program_status check (status in ('deleted','active')),
  constraint uq_program_name unique (name),
  constraint uq_program_slug unique (slug),
  constraint pk_program primary key (id)
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
  status                        varchar(7) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint ck_track_status check (status in ('deleted','active')),
  constraint pk_track primary key (id)
);

create table track_artist (
  track_id                      bigint not null,
  artist_id                     bigint not null,
  constraint pk_track_artist primary key (track_id,artist_id)
);

create table track_remixer (
  track_id                      bigint not null,
  artist_id                     bigint not null,
  constraint pk_track_remixer primary key (track_id,artist_id)
);

create table track_tracklist (
  track_id                      bigint not null,
  tracklist_id                  bigint not null,
  constraint pk_track_tracklist primary key (track_id,tracklist_id)
);

create table tracklist (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  slug                          varchar(191) not null,
  performed                     date,
  image                         varchar(255),
  user_id                       bigint not null,
  status                        varchar(7) not null,
  created                       datetime not null,
  updated                       datetime not null,
  constraint ck_tracklist_status check (status in ('deleted','active')),
  constraint uq_tracklist_slug unique (slug),
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

alter table artist_program add constraint fk_artist_program_artist foreign key (artist_id) references artist (id) on delete restrict on update restrict;
create index ix_artist_program_artist on artist_program (artist_id);

alter table artist_program add constraint fk_artist_program_program foreign key (program_id) references program (id) on delete restrict on update restrict;
create index ix_artist_program_program on artist_program (program_id);

alter table media add constraint fk_media_tracklist_id foreign key (tracklist_id) references tracklist (id) on delete restrict on update restrict;
create index ix_media_tracklist_id on media (tracklist_id);

alter table media add constraint fk_media_artist_id foreign key (artist_id) references artist (id) on delete restrict on update restrict;
create index ix_media_artist_id on media (artist_id);

alter table media add constraint fk_media_label_id foreign key (label_id) references label (id) on delete restrict on update restrict;
create index ix_media_label_id on media (label_id);

alter table media add constraint fk_media_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_media_user_id on media (user_id);

alter table program add constraint fk_program_channel_id foreign key (channel_id) references channel (id) on delete restrict on update restrict;
create index ix_program_channel_id on program (channel_id);

alter table token add constraint fk_token_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table track add constraint fk_track_genre_id foreign key (genre_id) references genre (id) on delete restrict on update restrict;
create index ix_track_genre_id on track (genre_id);

alter table track add constraint fk_track_label_id foreign key (label_id) references label (id) on delete restrict on update restrict;
create index ix_track_label_id on track (label_id);

alter table track_artist add constraint fk_track_artist_track foreign key (track_id) references track (id) on delete restrict on update restrict;
create index ix_track_artist_track on track_artist (track_id);

alter table track_artist add constraint fk_track_artist_artist foreign key (artist_id) references artist (id) on delete restrict on update restrict;
create index ix_track_artist_artist on track_artist (artist_id);

alter table track_remixer add constraint fk_track_remixer_track foreign key (track_id) references track (id) on delete restrict on update restrict;
create index ix_track_remixer_track on track_remixer (track_id);

alter table track_remixer add constraint fk_track_remixer_artist foreign key (artist_id) references artist (id) on delete restrict on update restrict;
create index ix_track_remixer_artist on track_remixer (artist_id);

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

alter table artist_program drop foreign key fk_artist_program_artist;
drop index ix_artist_program_artist on artist_program;

alter table artist_program drop foreign key fk_artist_program_program;
drop index ix_artist_program_program on artist_program;

alter table media drop foreign key fk_media_tracklist_id;
drop index ix_media_tracklist_id on media;

alter table media drop foreign key fk_media_artist_id;
drop index ix_media_artist_id on media;

alter table media drop foreign key fk_media_label_id;
drop index ix_media_label_id on media;

alter table media drop foreign key fk_media_user_id;
drop index ix_media_user_id on media;

alter table program drop foreign key fk_program_channel_id;
drop index ix_program_channel_id on program;

alter table token drop foreign key fk_token_user_id;

alter table track drop foreign key fk_track_genre_id;
drop index ix_track_genre_id on track;

alter table track drop foreign key fk_track_label_id;
drop index ix_track_label_id on track;

alter table track_artist drop foreign key fk_track_artist_track;
drop index ix_track_artist_track on track_artist;

alter table track_artist drop foreign key fk_track_artist_artist;
drop index ix_track_artist_artist on track_artist;

alter table track_remixer drop foreign key fk_track_remixer_track;
drop index ix_track_remixer_track on track_remixer;

alter table track_remixer drop foreign key fk_track_remixer_artist;
drop index ix_track_remixer_artist on track_remixer;

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

drop table if exists artist_program;

drop table if exists channel;

drop table if exists genre;

drop table if exists label;

drop table if exists media;

drop table if exists program;

drop table if exists token;

drop table if exists track;

drop table if exists track_artist;

drop table if exists track_remixer;

drop table if exists track_tracklist;

drop table if exists tracklist;

drop table if exists tracklist_artist;

drop table if exists tracklist_genre;

drop table if exists user;

