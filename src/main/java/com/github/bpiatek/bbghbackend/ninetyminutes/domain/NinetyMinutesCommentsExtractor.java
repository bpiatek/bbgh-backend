package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static org.apache.commons.text.StringEscapeUtils.*;
import static org.jsoup.Jsoup.parse;

import com.github.bpiatek.bbghbackend.model.Comment;
import com.github.bpiatek.bbghbackend.model.CommentHtmlExtractor;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
class NinetyMinutesCommentsExtractor implements CommentHtmlExtractor {

  @Override
  public List<Comment> getComments(String html) {
    List<Comment> comments = new ArrayList<>();

    final Document document = parse(html);
    final Elements elements = document.select("a");

    for (Element element : elements) {
      if (isComment(element)) {
        final String author = getAuthor(element);
        Comment comment = testGetComment(comments, html, author);
//        final String commentContent = getCommentContent(html, author);
//        if(commentContent.isBlank()) {
//          continue;
//        }
//        final Comment comment = new Comment(author, commentContent);

        comments.add(comment);
      }
    }

    return comments;
  }

  private Comment testGetComment(List<Comment> comments, String html, String author) {
    final int startingPoint = commentStartingPoint(html, author);
    try {
      final String possibleComment = html.substring(startingPoint);
      final int commentStartIndex = possibleComment.indexOf("<br>") + 4;
      final int commentEndIndex = possibleComment.indexOf("<p align=");
      final String comment = possibleComment.substring(commentStartIndex, commentEndIndex);
      final String cleanedComment = cleanCommentFromHtmlTags(comment).trim();

      final Comment o = new Comment(author, cleanedComment);
      if(comments.contains(o)) {
        final Comment comment1 = searchForCorrectComment(comments, cleanedComment.substring(0,20), author, html);
        return comment1;
      } else {
        return o;
      }

    } catch (StringIndexOutOfBoundsException e) {
      return null;
    }
  }

  private Comment searchForCorrectComment(List<Comment> comments, String commentContent, String author, String html) {
    boolean flag = true;
    Comment newCommenttttt = Comment.builder().build();
    while (flag) {
      final int indexToCutHtmlFrom = html.indexOf(commentContent) + commentContent.length();
      final String newHtml = html.substring(indexToCutHtmlFrom);

      final int newStartingPoint = commentStartingPoint(newHtml, author);
      final String newPossibleComment = newHtml.substring(newStartingPoint);

      final int newCommentStartIndex = newPossibleComment.indexOf("<br>") + 4;
      final int newCommentEndIndex = newPossibleComment.indexOf("<p align=");
      final String newComment = newPossibleComment.substring(newCommentStartIndex, newCommentEndIndex);
      final String newCleanedComment = cleanCommentFromHtmlTags(newComment);
      newCommenttttt = new Comment(author, newCleanedComment);
      if (newCleanedComment.substring(0, 20).equals(commentContent)) {
        return searchForCorrectComment(
            comments,
            newCleanedComment,
            author,
            newHtml
        );
      }
      flag = false;
    }
    return newCommenttttt;
  }


  private String getCommentContent(String html, String author) {
    final int startingPoint = commentStartingPoint(html, author);
    try {
      final String possibleComment = html.substring(startingPoint);
      final int commentStartIndex = possibleComment.indexOf("<br>") + 4;
      final int commentEndIndex = possibleComment.indexOf("<p align=");
      final String comment = possibleComment.substring(commentStartIndex, commentEndIndex);
      return cleanCommentFromHtmlTags(comment).trim();
    } catch (StringIndexOutOfBoundsException e) {
      return "";
    }
  }

  private String cleanCommentFromHtmlTags(String comment) {
    return parse(comment).text();
  }

  private int commentStartingPoint(String html, String author) {
    return html.indexOf("<a class=\"main\">" + author + "</a>");
  }

  private String getAuthor(Element element) {
    return unescapeHtml4(element.childNode(0).toString());
  }

  private boolean isComment(Element element) {
    return "main".equals(element.className()) && !(element.attributes().hasKey("href"));
  }
}
