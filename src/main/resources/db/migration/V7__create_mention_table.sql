create table mention (
    id bigint not null auto_increment,
    comment_id bigint not null,
    player_id bigint not null,
    sentiment enum('POSITIVE', 'NEUTRAL', 'NEGATIVE', 'NOT_CHECKED') default 'NOT_CHECKED',
    primary key (id)
);
