package com.inditex.mecc.mectlglnk.domain.repository;

import java.util.List;

import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;

public interface GridCategoryGroupRepository {

  void saveCategoriesByStoreId(List<CatGroupId> categories, StoreId storeId);


}
