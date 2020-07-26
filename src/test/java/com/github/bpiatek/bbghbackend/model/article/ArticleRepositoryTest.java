package com.github.bpiatek.bbghbackend.model.article;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.bpiatek.bbghbackend.model.comment.Comment;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Bartosz Piatek on 26/07/2020
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class ArticleRepositoryTest {

  private static final LocalDateTime ARTICLE_DATE = LocalDateTime.of(2020, 11, 3, 12, 33, 57);
  private static final String ARTICLE_TITLE = "Test title";
  private static final String COMMENT_AUTHOR = "Some author";
  private static final LocalDateTime COMMENT_DATE = LocalDateTime.of(2020, 11, 3, 22, 31, 15);

  @Autowired
  private ArticleRepository articleRepository;

  @Test
  void shouldCorrectlySaveArticle() {
    // given
    final Article article = createArticle("First Content");

    // when
    final Article savedArticle = articleRepository.save(article);

    // then
    final Article retrievedArticle = articleRepository.findById(savedArticle.getId()).orElse(Article.builder().build());
    SoftAssertions articleBundle = new SoftAssertions();
    articleBundle.assertThat(retrievedArticle.getCreationDate()).isEqualTo(ARTICLE_DATE);
    articleBundle.assertThat(retrievedArticle.getTitle()).isEqualTo(ARTICLE_TITLE);
    articleBundle.assertThat(retrievedArticle.getComments().get(0).getAuthor()).isEqualTo(COMMENT_AUTHOR);
    articleBundle.assertAll();
  }

  @Test
  void shouldCorrectlyRetrieveAllArticles() {
    // given
    final Article firstArticle = articleRepository.save(createArticle("First Content"));
    final Article secondArticle = articleRepository.save(createArticle("Second Content"));

    // when
    final Page<Article> allArticles = articleRepository.findAll(PageRequest.of(0, 2));

    // then
    assertThat(allArticles).contains(firstArticle, secondArticle);
  }

  private Article createArticle(String articleContent) {
    return Article.builder()
        .title(ARTICLE_TITLE)
        .content(articleContent)
        .creationDate(ARTICLE_DATE)
        .url("Some url")
        .comments(List.of(createComment()))
        .build();
  }

  private Comment createComment() {
    return Comment.builder()
        .author("Some author")
        .content("Comment content")
        .dateAdded(COMMENT_DATE)
        .build();
  }
}
