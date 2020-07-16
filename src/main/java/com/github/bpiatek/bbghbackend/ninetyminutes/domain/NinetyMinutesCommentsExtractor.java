package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static org.apache.commons.text.StringEscapeUtils.unescapeHtml4;
import static org.jsoup.Jsoup.parse;

import com.github.bpiatek.bbghbackend.model.Comment;
import com.github.bpiatek.bbghbackend.model.CommentHtmlExtractor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Slf4j
@Service
class NinetyMinutesCommentsExtractor implements CommentHtmlExtractor {

  private final TextToLocalDateTimeParser localDateTimeParser;

  private static final Pattern PATTERN = Pattern.compile("<a class=\"main\">(?<author>[^<]*)</a> - (?<date>.*?) - (?<authorHost>.*?)<br>\n(?<content>.*?)\n<p", Pattern.DOTALL);
  private static final int PATTERN_GROUPS_COUNT = 4;

  NinetyMinutesCommentsExtractor(TextToLocalDateTimeParser localDateTimeParser) {
    this.localDateTimeParser = localDateTimeParser;
  }

  @Override
  public List<Comment> getComments(String html) {
    Matcher matcher = PATTERN.matcher(html);
    List<Comment> comments = new ArrayList<>();

    while (matcher.find()) {
      if (matcher.groupCount() == PATTERN_GROUPS_COUNT) {

        Comment comment = Comment.builder()
            .author(matcher.group("author"))
            .content(matcher.group("content"))
            .dateAdded(localDateTimeParser.parse(matcher.group("date")))
            .build();

        if (!containsComment(comments, comment)) {
          comments.add(comment);
        }
      }
    }

    return comments;
  }

  private boolean containsComment(List<Comment> comments, Comment comment) {
    for(Comment o : comments) {
      if (
          o.getAuthor().equals(comment.getAuthor()) &&
          o.getContent().equals(comment.getContent()) &&
          o.getDateAdded().equals(comment.getDateAdded())
      ) {
        return true;
      }
    }
    return false;
  }
}
