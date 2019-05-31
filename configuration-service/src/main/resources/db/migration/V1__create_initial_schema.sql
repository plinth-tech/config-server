create table config
(
    id            bigint       not null auto_increment,
    creation_date timestamp    not null default current_timestamp,
    data_json     json         not null,
    platform      varchar(255) not null,
    version       bigint       not null,
    primary key (id),
    unique (platform, version)
);

create table base
(
    id            bigint    not null auto_increment,
    creation_date timestamp not null default current_timestamp,
    data_json     json      not null,
    version       bigint    not null,
    primary key (id)
);


