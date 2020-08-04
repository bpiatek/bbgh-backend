package com.github.bpiatek.bbghbackend.util.sql;

import java.util.*;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 *
 * Utility class to create simple sql select string.
 */
final public class SqlSelectStatement {
  public final List<String> select = new ArrayList<>();
  public final List<String> from = new ArrayList<>();
  public final List<String> where = new ArrayList<>();
  public final List<String> having = new ArrayList<>();
  public final List<String> orderBy = new ArrayList<>();
  public long limit =  0L;
  public long offset = 0L;

  String getSql() {
    String selectStmt = String.format("SELECT %s FROM %s", String.join(", ", select),String.join(", ", from));
    if (where.size() > 0) {
      selectStmt += String.format(" WHERE %s", String.join(" AND ", where));
    }
    if (having.size() > 0) {
      selectStmt += String.format(" HAVING %s", String.join(" AND ", having));
    }
    if (orderBy.size() > 0) {
      selectStmt += String.format(" ORDER BY %s", String.join(", ", orderBy));
    }
    if (limit > 0) {
      selectStmt += String.format(" LIMIT %d", limit);
    }
    if (offset > 0) {
      selectStmt += String.format(" OFFSET %d", offset);
    }

    return selectStmt;
  }

  public SqlSelectStatement select(String ...selects) {
    this.select.addAll(Arrays.asList(selects));
    return this;
  }

  public SqlSelectStatement from(String ...froms) {
    this.from.addAll(Arrays.asList(froms));
    return this;
  }

  public SqlSelectStatement where(String ...where) {
    this.where.addAll(Arrays.asList(where));
    return this;
  }

  public SqlSelectStatement having(String ...having) {
    this.having.addAll(Arrays.asList(having));
    return this;
  }

  public SqlSelectStatement orderBy(String ...orderBy) {
    this.orderBy.addAll(Arrays.asList(orderBy));
    return this;
  }

  public SqlSelectStatement limit(long limit) {
    this.limit = limit;
    return this;
  }

  public SqlSelectStatement offset(long offset) {
    this.offset = offset;
    return this;
  }

  @Override
  public String toString() {
    return this.getSql();
  }

  @Override
  public SqlSelectStatement clone() {
    SqlSelectStatement cloned = new SqlSelectStatement();

    cloned.select.addAll(this.select);
    cloned.from.addAll(this.from);
    cloned.where.addAll(this.where);
    cloned.having.addAll(this.having);
    cloned.orderBy.addAll(this.orderBy);
    cloned.limit = this.limit;
    cloned.offset = this.offset;

    return cloned;
  }
}
