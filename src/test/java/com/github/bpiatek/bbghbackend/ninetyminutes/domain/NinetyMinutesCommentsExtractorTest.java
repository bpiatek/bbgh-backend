package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.bpiatek.bbghbackend.model.Comment;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@SpringBootTest(classes = {
    TextToLocalDateTimeParser.class,
    NinetyMinutesCommentsExtractor.class
})
class NinetyMinutesCommentsExtractorTest {

  private static final int COMMENTS_IN_FILE_2 = 48;
  private static final int UNIQUE_COMMENTS_IN_FILE_1 = 158;
  private static final String COMMENT_AUTHOR =  "Danziger";
  private static final String COMMENT_CONTENT =  "40 tys euro plus bonusy, w Lechii nie miał nawet połowy tego. Kasa kasa kasa...";
  private static final LocalDateTime COMMENT_DATE = LocalDateTime.of(2020, 7, 2, 9, 50, 23);


  @Autowired
  private NinetyMinutesCommentsExtractor commentsExtractor;

  @Test
  void shouldCorrectlyGetOnlyUniqueCommentsFromFile2() {
    // given
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_2);
    final Comment shouldContainThisComment = new Comment(COMMENT_AUTHOR, COMMENT_CONTENT, COMMENT_DATE);

    // when
    final List<Comment> comments = commentsExtractor.getComments(html);

    //then
    SoftAssertions commentBundle = new SoftAssertions();
    commentBundle.assertThat(comments.size()).isEqualTo(COMMENTS_IN_FILE_2);
    commentBundle.assertThat(comments).contains(shouldContainThisComment);
    commentBundle.assertAll();
  }

  @Test
  void shouldCorrectlyGetOnlyUniqueCommentsFromFile1() {
    // given
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_1);

    // when
    final List<Comment> comments = commentsExtractor.getComments(html);

    // then
    assertThat(comments.size()).isEqualTo(UNIQUE_COMMENTS_IN_FILE_1);
  }

  @Test
  void isAbleToExtractCommentsFromFile3() {
    // given
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_3);

    // when
    final List<Comment> comments = commentsExtractor.getComments(html);

    // then
    assertThat(comments.size()).isGreaterThan(0);
  }
}
