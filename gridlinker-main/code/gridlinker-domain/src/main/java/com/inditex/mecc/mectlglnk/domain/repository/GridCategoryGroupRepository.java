package com.inditex.mecc.mectlglnk.domain.repository;

import java.util.List;

import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;

public interface GridCategoryGroupRepository {

  void saveGridCategoryGroupList(final List<GridCategoryGroup> gridCategoryGroupList);

}
