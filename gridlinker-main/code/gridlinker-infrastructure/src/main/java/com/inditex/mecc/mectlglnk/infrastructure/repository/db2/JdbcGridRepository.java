package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
import com.inditex.mecc.mectlglnk.domain.entity.Grid;
import com.inditex.mecc.mectlglnk.domain.entity.GridId;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;
import com.inditex.mecc.mectlglnk.domain.repository.GridRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcGridRepository implements GridRepository {

  private final Logger logger;

  private final NamedParameterJdbcOperations jdbcTemplate;

  static Grid rowMapper(final ResultSet resultSet, final int rowNum) throws SQLException {
    return Grid.builder()
        .gridId(GridId.builder().value(resultSet.getString(1)).build())
        .catalogId(CatGroupId.builder().value(resultSet.getInt(2)).build())
        .build();
  }

  @Override
  public List<Grid> findDistinctByStoreAndCategories(final List<StoreId> storeIds, Set<CatGroupId> catGroupIds) {
    try {

      List<Integer> categories = catGroupIds.stream().map(CatGroupId::getValue).toList();
      List<Integer> stores = storeIds.stream().map(StoreId::getValue).toList();

      SqlParameterSource parameters = new MapSqlParameterSource()
          .addValue(JdbcQueryConstants.STORE_ID, stores)
          .addValue(JdbcQueryConstants.CATGROUP_ID, categories);

      return this.jdbcTemplate.query(JdbcQueryConstants.GET_DISTINCT_GRID_IDS_QUERY, parameters, JdbcGridRepository::rowMapper);
    } catch (final EmptyResultDataAccessException e) {
     // this.logger.warn("Query GET_DISTINCT_GRID_IDS_QUERY for STORE_ID [{}] returned no values.", storeId, e);
      return Collections.emptyList();
    }
  }
}
