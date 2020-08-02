package com.github.bpiatek.bbghbackend.controller;

import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;

import com.github.bpiatek.bbghbackend.model.article.ArticleFacade;
import com.github.bpiatek.bbghbackend.model.article.search.ArticleSearchResult;
import com.github.bpiatek.bbghbackend.model.comment.CommentFacade;
import com.github.bpiatek.bbghbackend.model.article.Article;
import com.github.bpiatek.bbghbackend.model.comment.Comment;
import com.github.bpiatek.bbghbackend.swagger.ApiPageable;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Bartosz Piatek on 12/07/2020
 */
@Log4j2
@Api(tags = "Articles and comments controller")
@CrossOrigin
@RestController
@RequestMapping(value = "/api/articles")
class ArticlesController {

  private final ArticleFacade articleFacade;
  private final CommentFacade commentFacade;

  ArticlesController(ArticleFacade articleFacade, CommentFacade commentFacade) {
    this.articleFacade = articleFacade;
    this.commentFacade = commentFacade;
  }

  @ApiOperation(value = "Search articles")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved all articles"),
  })
  @ApiImplicitParam(name = "query", dataType = "string", paramType = "query", value = "negative < 20, comments > 40, creationDate > 2020-01-01")
  @ApiPageable
  @GetMapping
  Page<ArticleSearchResult> searchArticles(@ApiIgnore Pageable pageable, @ApiIgnore @RequestParam(value = "query", required = false) String query) {
    return articleFacade.search(pageable, query);
  }

  @ApiOperation(value = "Get article by ID")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved article by ID"),
  })
  @GetMapping("{articleId}")
  ResponseEntity<Article> getArticleById(@PathVariable Long articleId) {
    return ResponseEntity.ok().body(articleFacade.findById(articleId));
  }

  @ApiOperation(value = "Get all comments for given article")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved comments for a given article"),
  })
  @ApiPageable
  @GetMapping("{articleId}/comments")
  Page<Comment> getAllCommentsForArticlePageable(@PathVariable Long articleId, @ApiIgnore Pageable pageable) {
    return commentFacade.findByArticleId(articleId, pageable);
  }
}
