package com.github.bpiatek.bbghbackend.controller;

import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import com.github.bpiatek.bbghbackend.model.article.Article;
import com.github.bpiatek.bbghbackend.model.article.ArticleFacade;
import com.github.bpiatek.bbghbackend.model.comment.CommentFacade;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentResponse;
import com.github.bpiatek.bbghbackend.swagger.ApiPageable;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;

/**
 * Created by Bartosz Piatek on 12/07/2020
 */
@Log4j2
@Api(tags = "Articles controller")
@CrossOrigin
@RestController
@RequestMapping(value = "/api/articles")
@RequiredArgsConstructor
class ArticlesController {

  private final ArticleFacade articleFacade;
  private final CommentFacade commentFacade;

  @ApiOperation(value = "Search articles")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved all articles"),
  })
  @ApiImplicitParams({
      @ApiImplicitParam(name = "updatedAfter", dataType = "string", paramType = "query", value = "Date after which you want to see updated articles e.g. 2020-11-20T12:20:12"),
      @ApiImplicitParam(name = "newAfter", dataType = "string", paramType = "query", value = "Date after which you want to see new articles e.g. 2020-11-20T12:20:12."),
      @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", defaultValue = "0", value = "Results page you want to retrieve (0..N)"),
      @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", defaultValue = "20", value = "Number of records per page."),
      @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                                                                                                               + "Default sort order is ascending. " + "Multiple sort criteria are supported.")
  })
  @GetMapping
  Page<Article> getAllArticles(@ApiIgnore Pageable pageable,
                               // example of localDateTime: 2020-11-20T12:20:12
                               @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) LocalDateTime updatedAfter,
                               @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) LocalDateTime newAfter) {
    return articleFacade.search(pageable, updatedAfter, newAfter);
  }

  @ApiOperation(value = "Get article by ID")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved article by ID"),
  })
  @GetMapping("{articleId}")
  ResponseEntity<Article> getArticleById(@PathVariable Long articleId) {
    return ResponseEntity.ok(articleFacade.findById(articleId));
  }

  @ApiOperation(value = "Get all comments for given article")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved comments for a given article"),
  })
  @ApiPageable
  @GetMapping("{articleId}/comments")
  Page<CommentResponse> getAllCommentsForArticlePageable(@PathVariable Long articleId, @ApiIgnore Pageable pageable) {
    return commentFacade.findByArticleId(articleId, pageable);
  }

  @ApiOperation(value = "Get all articles for given player")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved articles for a given player"),
  })
  @ApiPageable
  @GetMapping("player/{playerId}")
  Page<Article> findAllByPlayerId(@PathVariable Long playerId, @ApiIgnore Pageable pageable) {
    return articleFacade.findByPlayerId(playerId, pageable);
  }

  @ApiOperation(value = "Get articles about Lech Poznań")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved articles"),
  })
  @GetMapping("aboutLechPoznan")
  Page<Article> findArticlesAboutLechPoznan(@ApiIgnore Pageable pageable) {
    return articleFacade.findArticlesAboutLechPoznan(pageable);
  }
}
