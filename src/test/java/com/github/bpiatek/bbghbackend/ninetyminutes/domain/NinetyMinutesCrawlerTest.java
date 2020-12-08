package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.HTML_EXAMPLE_FILE_4;
import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.HTML_EXAMPLE_FILE_4_EXTRA_COMMENT;
import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.readHtmlTestFile;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.bpiatek.bbghbackend.BbghBackendApplication;
import com.github.bpiatek.bbghbackend.model.article.Article;
import com.github.bpiatek.bbghbackend.model.article.ArticleFacade;
import com.github.bpiatek.bbghbackend.model.comment.CommentFacade;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentResponse;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Bartosz Piatek on 08/12/2020
 */
@SpringBootTest
@ContextConfiguration(classes = {BbghBackendApplication.class})
@ExtendWith(MockitoExtension.class)
@Transactional
class NinetyMinutesCrawlerTest {

  public static final String ARTICLE_URL = "http://www.90minut.pl/news/testpage";

  @Autowired
  private ArticleCreator articleCreator;

  @Autowired
  private ArticleFacade articleFacade;

  @Autowired
  private CommentFacade commentFacade;

  @Test
  void shouldCorrectlyExtractNewArticleAndSaveItInDatabase() {
    // given
    final NinetyMinutesCrawler crawler = new NinetyMinutesCrawler(articleCreator, articleFacade);

    // when
    crawler.visit(createPage(readHtmlTestFile(HTML_EXAMPLE_FILE_4)));

    // then
    Article article = articleFacade.findByUrl(ARTICLE_URL).stream().findFirst().get();
    Page<CommentResponse> commentResponse = commentFacade.findByArticleId(article.getId(), null);

    assertThat(commentResponse.getTotalElements()).isEqualTo(8);
  }

  @Test
  void shouldCorrectlyExtractNewCommentFromExistingArticle() {
    // given
    final NinetyMinutesCrawler crawler = new NinetyMinutesCrawler(articleCreator, articleFacade);
    crawler.visit(createPage(readHtmlTestFile(HTML_EXAMPLE_FILE_4)));
    articleFacade.findByUrl(ARTICLE_URL).stream().findFirst().get();

    // when
    crawler.visit(createPage(readHtmlTestFile(HTML_EXAMPLE_FILE_4_EXTRA_COMMENT)));

    // then
    Article article = articleFacade.findByUrl(ARTICLE_URL).stream().findFirst().get();
    Page<CommentResponse> commentResponse = commentFacade.findByArticleId(article.getId(), null);

    assertThat(commentResponse.getTotalElements()).isEqualTo(9);
  }


  private edu.uci.ics.crawler4j.crawler.Page createPage(String pageContent) {
    WebURL wevUrl = new WebURL();
    wevUrl.setURL(ARTICLE_URL);
    edu.uci.ics.crawler4j.crawler.Page page = new edu.uci.ics.crawler4j.crawler.Page(wevUrl);
    page.setParseData(createHtmlParseData(pageContent));

    return page;
  }

  private HtmlParseData createHtmlParseData(String html) {
    HtmlParseData htmlParseData = new HtmlParseData();
    htmlParseData.setHtml(html);

    return htmlParseData;
  }
}
