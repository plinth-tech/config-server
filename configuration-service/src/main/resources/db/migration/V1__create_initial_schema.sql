create table config (
    id bigint not null auto_increment,
    created_at timestamp,
    data_json json not null,
    platform varchar(255) not null,
    version bigint not null,
    primary key (id));