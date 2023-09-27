package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

import static com.inditex.mecc.mectlglnk.infrastructure.repository.db2.JdbcQueryConstants.XCATGROUP_GRID_STATEMENT;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
import com.inditex.mecc.mectlglnk.domain.entity.Grid;
import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;
import com.inditex.mecc.mectlglnk.domain.entity.GridId;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;
import com.inditex.mecc.mectlglnk.domain.repository.GridCategoryRepository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcGridCategoryRepository implements GridCategoryRepository {

  private final Logger logger;

  private final NamedParameterJdbcOperations jdbcTemplate;


  static GridCategoryGroup rowMapper(final ResultSet resultSet, final int rowNum) throws SQLException {
    return GridCategoryGroup.builder()
        .catGroupId(CatGroupId.builder().value(resultSet.getInt(1)).build())
        .gridCategory(Grid.builder().gridId(GridId.builder().value(resultSet.getString(2)).build()).build())
        .storeId(StoreId.builder().value(resultSet.getInt(3)).build()).build();
  }

  @Override
  public List<GridCategoryGroup> findIopCategoriesByStoreId(StoreId storeId) {
    try {

      Map<String, String> parameters = new HashMap<>();
      parameters.put(JdbcQueryConstants.STORE_ID, String.valueOf(storeId.getValue()));

      String query = StringSubstitutor.replace(XCATGROUP_GRID_STATEMENT, parameters, "{", "}");

      return this.jdbcTemplate.query(query, JdbcGridCategoryRepository::rowMapper);
    } catch (final EmptyResultDataAccessException e) {
      this.logger.warn("Query GET_DISTINCT_GRID_IDS_QUERY for STORE_ID [{}] returned no values.", storeId.getValue(), e);
      return Collections.emptyList();
    }
  }
}
