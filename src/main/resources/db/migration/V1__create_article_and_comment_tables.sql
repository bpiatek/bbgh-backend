create table article (
    id bigint not null auto_increment,
    content varchar(2048),
    creation_date datetime,
    title varchar(512),
    url varchar(512),
    primary key (id)
);

create table comment (
    id bigint not null auto_increment,
    author varchar(255),
    content varchar(2048),
    article_id bigint,
    primary key (id)
);

ALTER TABLE comment ADD CONSTRAINT fk_comment_to_article FOREIGN KEY (article_id) REFERENCES article(id);
