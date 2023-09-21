package com.inditex.mecc.mectlglnk.domain.repository;

import java.util.List;

import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;
import com.inditex.mecc.mectlglnk.domain.entity.GridId;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;

public interface CategoryRepository {

  List<GridCategoryGroup> findIopCategoriesByStoreId(StoreId storeId);

  boolean existGridBy(GridId gridId, StoreId storeId);
}
