package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import com.github.bpiatek.bbghbackend.model.Comment;
import com.github.bpiatek.bbghbackend.model.CommentHtmlExtractor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
class NinetyMinutesCommentsExtractor implements CommentHtmlExtractor {
  private static final int MAX_COMMENT_LENGTH = 2000;

  @Override
  public List<Comment> getComments(String html) {
    List<Comment> comments = new ArrayList<>();

    final Document document = Jsoup.parse(html);
    final Elements elements = document.select("a");

    for (Element element : elements) {
      if (isComment(element)) {
        final String author = getAuthor(element);
        final String commentContent = getCommentContent(html, author);
        final Comment comment = new Comment( author, commentContent);
        comments.add(comment);
      }
    }

    return comments;
  }

  private String getCommentContent(String html, String author) {
    final int startingPoint = commentStartingPoint(html, author);
    try {
      final String possibleComment = html.substring(startingPoint, startingPoint + MAX_COMMENT_LENGTH);
      final int commentStartIndex = possibleComment.indexOf("<br>") + 4;
      final int commentEndIndex = possibleComment.indexOf("<p");
      return cleanCommentFromHtmlTags(possibleComment.substring(commentStartIndex, commentEndIndex).trim());
    } catch (StringIndexOutOfBoundsException e) {
      return "";
    }
  }

  private String cleanCommentFromHtmlTags(String comment) {
    return Jsoup.parse(comment).text();
  }

  private int commentStartingPoint(String html, String author) {
    return html.indexOf("<a class=\"main\">" + author);
  }

  private String getAuthor(Element element) {
    return element.childNode(0).toString();
  }

  private boolean isComment(Element element) {
    return "main".equals(element.className()) && !(element.attributes().hasKey("href"));
  }
}
