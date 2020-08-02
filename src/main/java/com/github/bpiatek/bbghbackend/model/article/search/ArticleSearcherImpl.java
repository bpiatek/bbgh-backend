package com.github.bpiatek.bbghbackend.model.article.search;

import com.github.bpiatek.bbghbackend.model.comment.CommentOpinionStatus;
import com.github.bpiatek.bbghbackend.util.sql.SqlSelectStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Service
public class ArticleSearcherImpl implements ArticleSearcher {

  @PersistenceContext
  private final EntityManager em;

  public ArticleSearcherImpl(EntityManager entityManager) {
    this.em = entityManager;
  }

  @Override
  public List<ArticleSearchFilter> getFiltersFromSearchQuery(String query) {
    List<ArticleSearchFilter> filterList = new ArrayList<>();
    if (query == null) {
      return filterList;
    }
    query = query.replaceAll("\\s+","");
    Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)([^,]+?),");
    Matcher matcher = pattern.matcher(query + ",");
    while (matcher.find()) {
      filterList.add(new ArticleSearchFilter(matcher.group(1), matcher.group(2), matcher.group(3)));
    }
    return filterList;
  }

  @Override
  public Page<ArticleSearchResult> find(Pageable pageable, List<ArticleSearchFilter> filterList) {

    Map<String,String> fields = new LinkedHashMap<>() {{
      // fields are lowercase to simplify filter step
      String selectCountByStatus = "(SELECT COUNT(id) FROM comment WHERE article_id = article.id AND comment_opinion_status = '%s')";
      put("id", "id");
      put("url", "url");
      put("title", "title");
      put("creationdate", "creation_date");
      put("content", "content");
      put("comments", "(SELECT COUNT(id) FROM comment WHERE article_id = article.id)");
      put("positive", String.format(selectCountByStatus, CommentOpinionStatus.POSITIVE));
      put("neutral", String.format(selectCountByStatus, CommentOpinionStatus.NEUTRAL));
      put("negative", String.format(selectCountByStatus, CommentOpinionStatus.NEGATIVE));
      put("notopinion", String.format(selectCountByStatus, CommentOpinionStatus.NOT_OPINION));
      put("notchecked", String.format(selectCountByStatus, CommentOpinionStatus.NOT_CHECKED));
    }};
    // Map of future query parameters
    Map<String, Object> sqlQueryParams = new HashMap<>();

    SqlSelectStatement selectStatement = new SqlSelectStatement();
    for (Map.Entry<String,String> field : fields.entrySet()) {
      selectStatement.select(String.format("%s AS %s", field.getValue(), field.getKey()));
    }
    selectStatement.from("article");

    // where
    for (ArticleSearchFilter filter : filterList) {
      if (fields.containsKey(filter.getProperty().toLowerCase())) {
        String alias = filter.getProperty().toLowerCase();
        if (filter.getOperation().equalsIgnoreCase(">")) {
          selectStatement.where(String.format("%s > :%s", fields.get(alias), alias));
          sqlQueryParams.put(alias, filter.getValue());
        } else if (filter.getOperation().equalsIgnoreCase("<")) {
          selectStatement.where(String.format("%s < :%s", fields.get(alias), alias));
          sqlQueryParams.put(alias, filter.getValue());
        } else if (filter.getOperation().equalsIgnoreCase(":")) {
          selectStatement.where(String.format("%s LIKE :%s", fields.get(alias), alias));
          sqlQueryParams.put(alias, "%"+filter.getValue().toString()+"%");
        }
      }
    }

    // order by
    for (Sort.Order order : pageable.getSort()) {
      if (fields.containsKey(order.getProperty().toLowerCase())) {
        selectStatement.orderBy(String.format("%s %s", order.getProperty().toLowerCase(), order.getDirection()));
      }
    }

    selectStatement.limit(pageable.getPageSize());
    selectStatement.offset(pageable.getOffset());

    Query selectQuery = createQuery(selectStatement.toString(), sqlQueryParams);
    Query countQuery = createQuery(createSqlCountQuery(selectStatement).toString(), sqlQueryParams);

    long count = ((BigInteger) countQuery.getSingleResult()).longValue();

    // mapping sql select results to dto
    @SuppressWarnings("unchecked")
    List<Object[]> searchResult = selectQuery.getResultList();
    List<ArticleSearchResult> results = new ArrayList<>();
    for (Object[] b : searchResult) {
      results.add(ArticleSearchResult.builder()
          .id(((BigInteger) b[0]).longValue())
          .url((String) b[1])
          .title((String) b[2])
          .creationDate(((Timestamp) b[3]).toLocalDateTime())
          .content((String) b[4])
          .comments(((BigInteger) b[5]).longValue())
          .positive(((BigInteger) b[6]).longValue())
          .neutral(((BigInteger) b[7]).longValue())
          .negative(((BigInteger) b[8]).longValue())
          .notOpinion(((BigInteger) b[9]).longValue())
          .notChecked(((BigInteger) b[10]).longValue())
          .build()
      );
    }

    return new PageImpl<>(results, pageable, count);
  }

  private Query createQuery(String sql, Map<String,Object> params) {
    Query query = em.createNativeQuery(sql);
    // Bind parameters
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    return query;
  }

  private SqlSelectStatement createSqlCountQuery(SqlSelectStatement fromQuery) {
    SqlSelectStatement subquery = fromQuery.clone();
    subquery.orderBy.clear();
    subquery.select.clear();
    subquery.limit(0);
    subquery.offset(0);
    subquery.select("id");
    SqlSelectStatement countQuery = new SqlSelectStatement();
    countQuery
        .select("COUNT(id)")
        .from(String.format("(%s) articles", subquery));
    return countQuery;
  }
}
