package com.inditex.mecc.mectlglnk.domain.repository;

import java.util.List;

import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;

public interface GridCategoryRepository {

  List<GridCategoryGroup> findIopCategoriesByStoreId(StoreId storeId);
}