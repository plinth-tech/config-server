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
    id            bigint       not null auto_increment,
    creation_date timestamp    not null default current_timestamp,
    data_json     json         not null,
    version       bigint       not null,
    primary key (id)
);


insert into config (data_json, platform, version)
values (
           '{
             "title": "Goodbye!",
             "author" : {
               "givenName" : "John",
               "familyName" : "Doe"
             },
             "tags":[ "example", "sample" ],
             "content": "This will be unchanged"
           }',
           'plinth',
           1
       );

insert into base (data_json, version)
values(
          '       {
            "title": "Hello!",
            "phoneNumber": "+01-123-456-7890",
            "author": {
              "familyName": null
            },
            "tags": [ "example" ]
          }',
          1
      );


#todo primeiro exemplo para teste
/*
insert into config (data_json, platform, version)
values (
        '{
            "config1": "config1",
            "config2": "config2",
            "config3": {
                "config31": "config3.1",
                "config32": "config3.2"
            }
        }',
        'plinth',
        1
);

insert into base (data_json, version)
values(
      '{
           "config1": "config1",
           "config2": "config2",
           "config3": {
               "config31": [{"config311":"new config3.1.1"}],
               "config32": "config3.2"
           }
       }',
   1
);*/


#todo segundo exemple para teste
/*
insert into config (data_json, platform, version)
values (
           '{
             "config1": "config1",
             "config2": "config2",
             "config3": {
               "config31": [{
                 "config311":"config3.1.1",
                 "config313":"config3.1.3"}],
               "config32": "config3.2"
             }
           }',
           'plinth',
           1
       );

insert into base (data_json, version)
values(
          '{
            "config3": {
              "config31": [{
                "config311": [{
                  "config3111":"new config3.1.1.1",
                  "config3112":"new config3.1.1.2"
                }]
              }]
            },
            "config2": "new config2"
          }',
          1
      );*/