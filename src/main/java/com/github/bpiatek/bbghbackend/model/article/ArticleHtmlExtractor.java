package com.github.bpiatek.bbghbackend.model.article;

import java.time.LocalDateTime;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
public interface ArticleHtmlExtractor {
  String getArticleContentAsText(String html);
  String getArticleContentAsHtml(String html);
  LocalDateTime getArticleDateTime(String html);
}
