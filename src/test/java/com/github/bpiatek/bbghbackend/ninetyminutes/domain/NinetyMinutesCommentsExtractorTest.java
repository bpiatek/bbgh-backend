package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.HTML_EXAMPLE_FILE_1;
import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.HTML_EXAMPLE_FILE_2;
import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.readHtmlTestFile;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.bpiatek.bbghbackend.model.Comment;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
class NinetyMinutesCommentsExtractorTest {

  private static final int COMMENTS_IN_FILE_2 = 48;

  @Test
  void shouldCorrectlyGetComments() {
    // given
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_2);
    final NinetyMinutesCommentsExtractor commentsExtractor = new NinetyMinutesCommentsExtractor();
    final Comment shouldContainThisComment = new Comment("Danziger","40 tys euro plus bonusy, w Lechii nie miał nawet połowy tego. Kasa kasa kasa...");

    // when
    final List<Comment> comments = commentsExtractor.getComments(html);

    //then
    SoftAssertions commentBundle = new SoftAssertions();
    commentBundle.assertThat(comments.size()).isEqualTo(COMMENTS_IN_FILE_2);
    commentBundle.assertThat(comments).contains(shouldContainThisComment);
    commentBundle.assertAll();
  }

  @Test
   void test() {
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_1);
    final NinetyMinutesCommentsExtractor commentsExtractor = new NinetyMinutesCommentsExtractor();
    final Document document = Jsoup.parse(html);
    final Element element = document.getElementsByClass("main").get(48);
    final Elements elements = element.nextElementSiblings();

    final List<Comment> comments = commentsExtractor.getComments(html);

    assertThat(false);
  }
}
