create table player (
    id bigint not null auto_increment,
    url_id bigint not null,
    first_name varchar(128),
    last_name varchar(128),
    date_of_birth date,
    current_team varchar(128),
    primary key (id)
);
