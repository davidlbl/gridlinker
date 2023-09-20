package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

import static com.inditex.mecc.mectlglnk.infrastructure.repository.db2.JdbcQueryConstants.XCATGROUP_GRID_STATEMENT;

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
import org.apache.commons.text.StringSubstitutor;
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

      Map<String, String> parameters = new HashMap<>();
      parameters.put(JdbcQueryConstants.STORE_ID, String.valueOf(storeId.getValue()));

      String query =StringSubstitutor.replace(XCATGROUP_GRID_STATEMENT, parameters, "{", "}");

      return this.jdbcTemplate.query(query, JdbcGridRepository::rowMapper);
    } catch (final EmptyResultDataAccessException e) {
      this.logger.warn("Query GET_DISTINCT_GRID_IDS_QUERY for STORE_ID [{}] returned no values.", storeId.getValue(), e);
      return Collections.emptyList();
    }
  }

  @Override
  public boolean isValidCategoryByStoreId(CatGroupId catGroupId, StoreId storeId) {
    try {
      Map<String, String> pathVariables = new HashMap<>();
      pathVariables.put("storeId", String.valueOf(storeId.getValue()));
      pathVariables.put("catgroupId",String.valueOf(catGroupId.getValue()));

      ResponseEntity<String> response = restClient.getForEntity(
          "/{storeId}/category/{catgroupId}",
          String.class,
          pathVariables);

      return response.getStatusCode().is2xxSuccessful();
    }catch (Exception e) {
        return false;
    }
  }
}
