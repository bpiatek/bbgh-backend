package com.github.bpiatek.bbghbackend.model.article.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
public interface ArticleSearcher {
  Page<ArticleSearchResult> find(Pageable pageable, List<ArticleSearchFilter> queryList);

  List<ArticleSearchFilter> getFiltersFromSearchQuery(String query);

  default Page<ArticleSearchResult> find(Pageable pageable, String query) {
    return find(pageable, getFiltersFromSearchQuery(query));
  }
}
