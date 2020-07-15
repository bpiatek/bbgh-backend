package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.HTML_EXAMPLE_FILE_1;
import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.HTML_EXAMPLE_FILE_2;
import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.readHtmlTestFile;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@SpringBootTest(classes = {
    TextToLocalDateTimeParser.class,
    NinetyMinutesArticleExtractor.class
})
class NinetyMinutesArticleExtractorTest {

  @Autowired
  private NinetyMinutesArticleExtractor articleExtractor;

  @Test
  void shouldCorrectlyParseDate() {
    // given
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_1);
    final LocalDateTime expectedDate = LocalDateTime.of(2020, 7, 3, 19, 54, 19);

    // when
    final LocalDateTime actualDate = articleExtractor.getArticleDateTime(html);

    // then
    assertThat(actualDate).isEqualTo(expectedDate);
  }

  @Test
  void shouldCorrectlyParseArticleContentFromFile1() {
    // given
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_1);

    // when
    final String actualContent = articleExtractor.getArticleContentAsText(html);
    // then
    SoftAssertions articleBundle = new SoftAssertions();
    articleBundle.assertThat(actualContent).contains("W meczu 34. kolejki PKO Ekstraklasy");
    articleBundle.assertThat(actualContent).doesNotContain("<b>W meczu 34. kolejki PKO Ekstraklasy");
    articleBundle.assertAll();
  }

  @Test
  void shouldCorrectlyParseArticleContentFromFile2() {
    // given
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_2);

    // when
    final String actualContent = articleExtractor.getArticleContentAsText(html);

    // then
    SoftAssertions articleBundle = new SoftAssertions();
    articleBundle.assertThat(actualContent).contains("Mladenović ma na koncie dziesięć występów w reprezentacji Serbii.");
    articleBundle.assertThat(actualContent).doesNotContain("Mladenović ma na koncie dziesięć występów w reprezentacji Serbii.<br>");
    articleBundle.assertAll();
  }
}
