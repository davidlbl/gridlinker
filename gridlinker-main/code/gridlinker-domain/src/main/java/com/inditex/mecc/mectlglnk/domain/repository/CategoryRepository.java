package com.inditex.mecc.mectlglnk.domain.repository;

import java.util.List;

import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;

public interface CategoryRepository {

  List<CatGroupId> findIopCategoriesByStoreId(StoreId storeId);

  boolean isValidCategoryByStoreId(CatGroupId catGroupId, StoreId storeId);

}
