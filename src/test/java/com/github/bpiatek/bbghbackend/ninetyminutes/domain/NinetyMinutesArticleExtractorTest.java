package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.HTML_EXAMPLE_FILE_1;
import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.HTML_EXAMPLE_FILE_2;
import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.readHtmlTestFile;
import static org.assertj.core.api.Assertions.assertThat;

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
  public void shouldCorrectlyParseDate() {
    // given
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_1);
    final LocalDateTime expectedDate = LocalDateTime.of(2020, 7, 3, 19, 54, 19);

    // when
    final LocalDateTime actualDate = articleExtractor.getArticleDateTime(html);

    // then
    assertThat(actualDate).isEqualTo(expectedDate);
  }

  @Test
  public void shouldCorrectlyParseArticleContentFromFile1() {
    // given
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_1);

    // when
    final String actualContent = articleExtractor.getArticleContentAsText(html);
    // then
    assertThat(actualContent).contains("W meczu 34. kolejki PKO Ekstraklasy");
    assertThat(actualContent).doesNotContain("<b>W meczu 34. kolejki PKO Ekstraklasy");
  }

  @Test
  public void shouldCorrectlyParseArticleContentFromFile2() {
    // given
    final String html = readHtmlTestFile(HTML_EXAMPLE_FILE_2);

    // when
    final String actualContent = articleExtractor.getArticleContentAsText(html);

    // then
    assertThat(actualContent).contains("Mladenović ma na koncie dziesięć występów w reprezentacji Serbii.");
    assertThat(actualContent).doesNotContain("Mladenović ma na koncie dziesięć występów w reprezentacji Serbii.<br>");
  }
}
