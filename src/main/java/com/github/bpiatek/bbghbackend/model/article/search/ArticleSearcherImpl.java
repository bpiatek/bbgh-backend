package com.github.bpiatek.bbghbackend.model.article.search;

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

import static com.github.bpiatek.bbghbackend.model.comment.CommentOpinionStatus.*;
import static java.lang.String.format;

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
    query = query.replaceAll("\\s+","").toLowerCase();
    Pattern pattern = Pattern.compile("(\\w+?)(:|<|>|=)([^,]+?),");
    Matcher matcher = pattern.matcher(query + ",");
    while (matcher.find()) {
      filterList.add(new ArticleSearchFilter(matcher.group(1), matcher.group(2), matcher.group(3)));
    }
    return filterList;
  }

  private Map<String, String> createFieldsMap() {
    // Using LinkedHashMap because we need to keep elements order
    Map<String, String> fields = new LinkedHashMap<>();
    String selectCountByStatus = "(SELECT COUNT(id) FROM comment WHERE article_id = article.id AND comment_opinion_status = '%s')";
    // fields are lowercase to simplify filter step
    // alias, field
    fields.put("id", "id");
    fields.put("url", "url");
    fields.put("title", "title");
    fields.put("creationdate", "creation_date");
    fields.put("content", "content");
    fields.put("comments", "(SELECT COUNT(id) FROM comment WHERE article_id = article.id)");
    fields.put("positive", format(selectCountByStatus, POSITIVE));
    fields.put("neutral", format(selectCountByStatus, NEUTRAL));
    fields.put("negative", format(selectCountByStatus, NEGATIVE));
    fields.put("notopinion", format(selectCountByStatus, NOT_OPINION));
    fields.put("notchecked", format(selectCountByStatus, NOT_CHECKED));

    return fields;
  }

  @Override
  public Page<ArticleSearchResult> find(Pageable pageable, List<ArticleSearchFilter> filterList) {

    SqlSelectStatement selectStatement = new SqlSelectStatement();
    Map<String, Object> selectStatementParams2 = new HashMap<>();
    List<Object> selectStatementParams = new ArrayList<>();

    // select
    Map<String,String> fields = createFieldsMap();
    for (Map.Entry<String,String> field : fields.entrySet()) {
      selectStatement.select(format("%s AS %s", field.getValue(), field.getKey()));
    }

    //from
    selectStatement.from("article");

    // where
    for (ArticleSearchFilter filter : filterList) {
      String alias = filter.getProperty();
      if (!fields.containsKey(alias)) {
        continue;
      }
      String sqlOperation = "";
      boolean canCompareFields = false;
      switch (filter.getOperation()) {
        case ">":
        case "<":
        case "=":
          sqlOperation = filter.getOperation();
          canCompareFields = true;
          break;
        case ":":
          sqlOperation = "LIKE";
          break;
      }
      if (sqlOperation.length() > 0) {
        // if we can compare fields and filter value is other field name
        if (canCompareFields && (filter.getValue() instanceof String) &&  fields.containsKey(filter.getValue().toString())) {
          selectStatement.where(format("%s %s %s", fields.get(alias), sqlOperation, fields.get(filter.getValue().toString())));
        } else {
          selectStatement.where(format("%s %s :%s", fields.get(alias), sqlOperation, selectStatementParams.size()));
          selectStatementParams.add(filter.getValue());
          selectStatementParams2.put(alias, filter.getValue());
        }
      }
    }

    // order by
    for (Sort.Order order : pageable.getSort()) {
      String alias = order.getProperty().toLowerCase();
      if (fields.containsKey(alias)) {
        selectStatement.orderBy(format("%s %s", alias, order.getDirection()));
      }
    }

    // limit and offset
    selectStatement.limit(pageable.getPageSize());
    selectStatement.offset(pageable.getOffset());


    // count first to avoid useless result query
    long count = getCount(selectStatement, selectStatementParams);
    if (count == 0) {
      return new PageImpl<>(new ArrayList<>(), pageable, 0L);
    }

    return new PageImpl<>(getArticleSearchResultList(selectStatement, selectStatementParams), pageable, count);
  }

  private long getCount(SqlSelectStatement sqlSelectStatement, List<Object> params) {
    Query countQuery = createNativeQuery(createSqlCountQuery(sqlSelectStatement).toString(), params);
    return ((BigInteger) countQuery.getSingleResult()).longValue();
  }

  private List<ArticleSearchResult> getArticleSearchResultList(SqlSelectStatement selectStatement, List<Object> params) {
    List<ArticleSearchResult> articleSearchResultList = new ArrayList<>();
    Query selectQuery = createNativeQuery(selectStatement.toString(), params);
    // mapping sql select results to ArticleSearchResult
    @SuppressWarnings("unchecked")
    List<Object[]> searchResults = selectQuery.getResultList();
    for (Object[] result : searchResults) {
      articleSearchResultList.add(mapQueryResult(result));
    }
    return articleSearchResultList;
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
        .select("COUNT(*)")
        .from(format("(%s) articles", subquery));
    return countQuery;
  }

  private ArticleSearchResult mapQueryResult(Object[] result) {
    return ArticleSearchResult.builder()
        .id(((BigInteger) result[0]).longValue())
        .url((String) result[1])
        .title((String) result[2])
        .creationDate(((Timestamp) result[3]).toLocalDateTime())
        .content((String) result[4])
        .comments(((BigInteger) result[5]).longValue())
        .positive(((BigInteger) result[6]).longValue())
        .neutral(((BigInteger) result[7]).longValue())
        .negative(((BigInteger) result[8]).longValue())
        .notOpinion(((BigInteger) result[9]).longValue())
        .notChecked(((BigInteger) result[10]).longValue())
        .build();
  }

  private Query createNativeQuery(String sql, List<Object> params) {
    Query query = em.createNativeQuery(sql);
    for(int i = 0; i < params.size(); i++) {
      query.setParameter(String.valueOf(i), params.get(i));
    }
    return query;
  }
}
