create table config (
    id bigint not null auto_increment,
    created_at timestamp,
    data_json json not null,
    tenant varchar(255) not null,
    version bigint not null,
    primary key (id)
) engine=InnoDB AUTO_INCREMENT=1;