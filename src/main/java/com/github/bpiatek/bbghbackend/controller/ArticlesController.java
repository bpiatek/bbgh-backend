package com.github.bpiatek.bbghbackend.controller;

import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;

import com.github.bpiatek.bbghbackend.dao.ArticleRepository;
import com.github.bpiatek.bbghbackend.dao.CommentRepository;
import com.github.bpiatek.bbghbackend.model.Article;
import com.github.bpiatek.bbghbackend.model.Comment;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Bartosz Piatek on 12/07/2020
 */
@Log4j2
@Api(tags = "Articles and comments controller")
@RestController
@RequestMapping(value = "/api/articles")
class ArticlesController {

  private final ArticleRepository articleRepository;
  private final CommentRepository commentRepository;

  ArticlesController(ArticleRepository articleRepository,
                     CommentRepository commentRepository) {
    this.articleRepository = articleRepository;
    this.commentRepository = commentRepository;
  }

  @ApiOperation(value = "Get all articles")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved all articles"),
  })
  @GetMapping
  Page<Article> getAllArticlesPageable(Pageable pageable) {
    return articleRepository.findAll(pageable);
  }

  @ApiOperation(value = "Run crawler for 90minut.pl")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved comments for a given article"),
  })
  @GetMapping("{articleId}/comments")
  Page<Comment> getAllCommentsForArticlePageable(@PathVariable Long articleId, Pageable pageable) {
    return commentRepository.findByArticleId(articleId, pageable);
  }
}
