package com.inditex.mecc.mectlglnk.domain.repository;

import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;

public interface CategoryRepository {

  boolean existCategoryBy(GridCategoryGroup gridCategoryGroup);

}
