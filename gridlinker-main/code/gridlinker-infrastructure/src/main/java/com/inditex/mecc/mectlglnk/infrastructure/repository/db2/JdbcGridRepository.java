package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inditex.aqsw.framework.common.rest.client.RestClient;
import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;
import com.inditex.mecc.mectlglnk.domain.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcGridRepository implements CategoryRepository {

  private final Logger logger;

  private final NamedParameterJdbcOperations jdbcTemplate;

  private final RestClient restClient;

  static CatGroupId rowMapper(final ResultSet resultSet, final int rowNum) throws SQLException {
    return CatGroupId.builder()
        .value(resultSet.getInt(1)).build();
  }

  @Override
  public List<CatGroupId> findIopCategoriesByStoreId(StoreId storeId) {
    try {

      SqlParameterSource parameters = new MapSqlParameterSource()
          .addValue(JdbcQueryConstants.STORE_ID, storeId.getValue());

      return this.jdbcTemplate.query(JdbcQueryConstants.XCATGROUP_GRID_DELETE_STATEMENT, parameters, JdbcGridRepository::rowMapper);
    } catch (final EmptyResultDataAccessException e) {
      this.logger.warn("Query GET_DISTINCT_GRID_IDS_QUERY for STORE_ID [{}] returned no values.", storeId.getValue(), e);
      return Collections.emptyList();
    }
  }

  @Override
  public boolean isValidCategoryByStoreId(CatGroupId catGroupId, StoreId storeId) {

    Map<String, String> pathVariables = new HashMap<>();
    pathVariables.put("storeId", String.valueOf(18702));
    pathVariables.put("catgroupId",String.valueOf(2297834));

    ResponseEntity<String> forEntity = restClient.getForEntity(
        "/{storeId}/category/{catgroupId}",
        String.class,
        pathVariables);

    return forEntity.getStatusCode().is2xxSuccessful();
  }
}
