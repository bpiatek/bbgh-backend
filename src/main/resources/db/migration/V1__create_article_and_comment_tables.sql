create table article (
    id bigint not null auto_increment,
    content varchar(15000),
    creation_date datetime,
    title varchar(512),
    url varchar(512),
    primary key (id)
);

create table comment (
    id bigint not null auto_increment,
    author varchar(255),
    content varchar(10000),
    article_id bigint,
    primary key (id)
);

ALTER TABLE comment ADD CONSTRAINT fk_comment_to_article FOREIGN KEY (article_id) REFERENCES article(id);
