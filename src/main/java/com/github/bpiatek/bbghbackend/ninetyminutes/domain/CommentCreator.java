package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.model.comment.CommentOpinionStatus.NOT_CHECKED;
import static org.jsoup.Jsoup.parse;

import com.github.bpiatek.bbghbackend.model.comment.Comment;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;

/**
 * Created by Bartosz Piatek on 17/07/2020
 */
@Service
class CommentCreator {
  private final TextToLocalDateTimeParser localDateTimeParser;

  CommentCreator(TextToLocalDateTimeParser localDateTimeParser) {
    this.localDateTimeParser = localDateTimeParser;
  }

  Comment createFromHtml(Matcher matcher) {
    return Comment.builder()
        .author(matcher.group("author"))
        .content(parse(matcher.group("content")).text())
        .dateAdded(localDateTimeParser.parseToLocalDateTime(matcher.group("date")))
        .commentOpinionStatus(NOT_CHECKED)
        .build();
  }
}
