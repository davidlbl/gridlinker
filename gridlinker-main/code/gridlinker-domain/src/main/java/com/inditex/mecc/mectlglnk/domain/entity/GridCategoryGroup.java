package com.inditex.mecc.mectlglnk.domain.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GridCategoryGroup {

  StoreId storeId;

  CatalogId catalogId;

  Grid gridCategory;

  GridStatus status;

  CatGroupId catGroupId;

  String settings;

  public enum GridStatus {
    LIVE,
    PREVIEW
  }

}
