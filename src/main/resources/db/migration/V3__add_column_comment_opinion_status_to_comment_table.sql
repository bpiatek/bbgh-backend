ALTER TABLE comment
ADD comment_opinion_status enum('OPINION', 'NOT_OPINION', 'NOT_CHECKED') DEFAULT 'NOT_CHECKED';
