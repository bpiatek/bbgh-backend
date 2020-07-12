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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Slf4j
@Service
class NinetyMinutesCommentsExtractor implements CommentHtmlExtractor {

  @Override
  public List<Comment> getComments(String html) {
    List<Comment> comments = new ArrayList<>();
    final Elements aElements = getPageElements(html, "a");

    for (Element element : aElements) {
      if (isComment(element)) {
        String author = getAuthor(element);
        Comment comment = extractSingleComment(comments, html, author);
        if(isDuplicated(comment)) {
          continue;
        }
        comments.add(comment);
      }
    }

    return comments;
  }

  private Comment extractSingleComment(List<Comment> comments, String html, String author) {
    final int startingPoint = commentStartingPoint(html, author);
    try {
      String firstCommentContentForAuthor = getFirstCommentContentForAuthor(html, startingPoint);
      Comment firstComment = new Comment(author, firstCommentContentForAuthor);
      if(comments.contains(firstComment)) {
        return searchForCorrectComment(firstCommentContentForAuthor.substring(0,20), author, html);
      } else {
        return firstComment;
      }
    } catch (StringIndexOutOfBoundsException e) {
      log.debug("Probably duplicated comment for author: {} in comment section for article: {}", author, parse(html).title());
      return null;
    }
  }

  private Comment searchForCorrectComment(String commentContent, String author, String html) {
    final int indexToCutHtmlTo = html.indexOf(commentContent) + commentContent.length();
    final String newHtmlToSearch = html.substring(indexToCutHtmlTo);
    final int newCommentStartingPoint = commentStartingPoint(newHtmlToSearch, author);
    final String newPossibleComment = newHtmlToSearch.substring(newCommentStartingPoint);
    final String nextCommentContent = extractCommentContent(newPossibleComment);

    if (nextCommentContent.substring(0, 20).equals(commentContent)) {
      return searchForCorrectComment(
          nextCommentContent,
          author,
          newHtmlToSearch
      );
    }

    return new Comment(author, nextCommentContent);
  }

  private String getFirstCommentContentForAuthor(String html, int startingPoint) {
    final String possibleComment = html.substring(startingPoint);
    return extractCommentContent(possibleComment);
  }

  private String extractCommentContent(String possibleComment) {
    final int commentStartIndex = possibleComment.indexOf("<br>") + 4;
    final int commentEndIndex = possibleComment.indexOf("<p align=");
    final String comment = possibleComment.substring(commentStartIndex, commentEndIndex);
    return cleanCommentFromHtmlTags(comment).trim();
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

  private Elements getPageElements(String html, String cssQuery) {
    final Document document = parse(html);
    return document.select(cssQuery);
  }

  private boolean isDuplicated(Comment comment) {
    return comment == null;
  }
}
