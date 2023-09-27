package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inditex.aqsw.framework.common.rest.client.RestClient;
import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;
import com.inditex.mecc.mectlglnk.domain.repository.GridRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeccanoGridRepository implements GridRepository {

  private final RestClient gridCoreRestClient;

  @Override
  public boolean existGridBy(GridCategoryGroup gridCategoryGroup) {
    return this.hasCommercialConfiguration(gridCategoryGroup);
  }

  private boolean hasCommercialConfiguration(GridCategoryGroup gridCategoryGroup) {
    try {
      Map<String, Object> request = this.buildRequestFrom(gridCategoryGroup);

      ResponseEntity<Map> response =
          gridCoreRestClient.postForEntity("/amiga/grpcgateway", request, Map.class);

      return this.hasGridCommercialConfigurations(response);

    }catch (Exception e) {
      return false;
    }
  }

  private Map<String, Object> buildRequestFrom(GridCategoryGroup gridCategoryGroup) {
    Map<String, String> payload = new HashMap<>();
    payload.put("store_id", String.valueOf(gridCategoryGroup.getStoreId().getValue()));
    payload.put("gridId", gridCategoryGroup.getGridCategory().getGridId().getValue());
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
