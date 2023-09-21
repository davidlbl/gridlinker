package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

import static com.inditex.mecc.mectlglnk.infrastructure.repository.db2.JdbcQueryConstants.XCATGROUP_GRID_STATEMENT;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.inditex.aqsw.framework.common.rest.client.RestClient;
import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
import com.inditex.mecc.mectlglnk.domain.entity.Grid;
import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;
import com.inditex.mecc.mectlglnk.domain.entity.GridId;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;
import com.inditex.mecc.mectlglnk.domain.repository.CategoryRepository;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.apache.kafka.common.protocol.types.Field.Str;
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

      return this.jdbcTemplate.query(query, JdbcGridRepository::rowMapper);
    } catch (final EmptyResultDataAccessException e) {
      this.logger.warn("Query GET_DISTINCT_GRID_IDS_QUERY for STORE_ID [{}] returned no values.", storeId.getValue(), e);
      return Collections.emptyList();
    }
  }

  @Override
  public boolean existGridBy(GridId gridId, StoreId storeId) {
    try {
      Map<String, Object> request = this.buildRequestFrom(gridId, storeId);

      logger.info("Request: gridId: {} - storeId: {}", gridId, storeId);
      ResponseEntity<Map> response =
          restClient.postForEntity("/amiga/grpcgateway", request, Map.class);

      boolean hasGridCommercialConfigurations = this.hasGridCommercialConfigurations(response);
      logger.info("hasGridDefinition: {}", hasGridCommercialConfigurations);

      return hasGridCommercialConfigurations;
    }catch (Exception e) {
      return false;
    }
  }

  private Map<String, Object> buildRequestFrom(GridId gridId, StoreId storeId) {
    Map<String, String> payload = new HashMap<>();
    payload.put("store_id", String.valueOf(storeId.getValue()));
    payload.put("gridId", gridId.getValue());
    payload.put("show_full_content", "false");

    Map<String, Object> request = new HashMap<>();
    request.put("fullMethodName", "appmicmecctlg.v1.griddefinition.GridDefinitionEndPoint/GetGridConfigurationList");
    request.put("payload", payload);

    return request;
  }

  private boolean hasGridCommercialConfigurations(ResponseEntity<Map> data){
    List<Object> l = (List<Object>) data.getBody().get("payload");
    Map<String, Object> grids = (Map<String, Object>) l.get(0);

    List<Object> commercialConfigurations = (List<Object>) grids.get("commercial_configurations");
    return !commercialConfigurations.isEmpty();
  }
}
