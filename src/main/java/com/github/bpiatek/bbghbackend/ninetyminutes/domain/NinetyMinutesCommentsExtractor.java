package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import com.github.bpiatek.bbghbackend.model.comment.Comment;
import com.github.bpiatek.bbghbackend.model.comment.CommentHtmlExtractor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Slf4j
@Service
@AllArgsConstructor
class NinetyMinutesCommentsExtractor implements CommentHtmlExtractor {

  private static final Pattern PATTERN = Pattern.compile("<a class=\"main\">(?<author>[^<]*)</a> - (?<date>.*?) - (?<authorHost>.*?)<br>\n(?<content>.*?)\n<p", Pattern.DOTALL);
  private static final int PATTERN_GROUPS_COUNT = 4;

  private final CommentCreator commentCreator;

  @Override
  public List<Comment> getComments(String html) {
    List<Comment> comments = new ArrayList<>();
    Matcher matcher = PATTERN.matcher(html);

    while (matcher.find()) {
      if (matcher.groupCount() == PATTERN_GROUPS_COUNT) {
        Comment comment = commentCreator.createFromHtml(matcher);

        if (!comments.contains(comment)) {
          comments.add(comment);
        }
      }
    }
    return comments;
  }
}
