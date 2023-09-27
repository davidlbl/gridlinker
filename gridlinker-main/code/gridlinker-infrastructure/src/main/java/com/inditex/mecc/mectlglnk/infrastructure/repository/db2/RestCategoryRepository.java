package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inditex.aqsw.framework.common.rest.client.RestClient;
import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;
import com.inditex.mecc.mectlglnk.domain.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
public class RestCategoryRepository implements CategoryRepository {

  private final RestClient navigationRestClient;

  @Override
  public boolean existCategoryBy(GridCategoryGroup gridCategoryGroup) {
    return this.isValidCategoryByStoreId(gridCategoryGroup);
  }

  private boolean isValidCategoryByStoreId(GridCategoryGroup gridCategoryGroup) {
    try {
      Map<String, Object> request = this.buildRequestFrom(gridCategoryGroup);

      ResponseEntity<Map> response =
          navigationRestClient.postForEntity("/mecnavreader/amiga/grpcgateway", request, Map.class);

      return !this.isHiddenCategory(response);

    }catch (Exception e) {
      return false;
    }
  }

  private Map<String, Object> buildRequestFrom(GridCategoryGroup gridCategoryGroup) {

    Map<String, String> storeId = new HashMap<>();
    storeId.put("value", String.valueOf(gridCategoryGroup.getStoreId().getValue()));

    Map<String, String> categoryId = new HashMap<>();
    categoryId.put("value", String.valueOf(gridCategoryGroup.getCatGroupId().getValue()));

    Map<String, Object> payload = new HashMap<>();
    payload.put("store_id", storeId);
    payload.put("filter_by_item_id", categoryId);
    //payload.put("filter_by_language_tag", "en-GB");

    Map<String, Object> request = new HashMap<>();
    request.put("fullMethodName", "mecnavreader.v1.NavigationService/GetNavigationCollection");
    request.put("payload", payload);

    return request;
  }

  private boolean isHiddenCategory(ResponseEntity<Map> data){
    List<Object> payload = (List<Object>) data.getBody().get("payload");
    return CollectionUtils.isEmpty(payload);
  }
}
