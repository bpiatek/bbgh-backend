ALTER TABLE comment
MODIFY COLUMN comment_opinion_status
    enum('POSITIVE', 'NEUTRAL', 'NEGATIVE', 'NOT_OPINION', 'NOT_CHECKED') default 'NOT_CHECKED' null
;