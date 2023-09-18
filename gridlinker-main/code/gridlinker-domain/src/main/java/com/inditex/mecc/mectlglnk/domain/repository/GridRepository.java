package com.inditex.mecc.mectlglnk.domain.repository;

import java.util.List;
import java.util.Set;

import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
import com.inditex.mecc.mectlglnk.domain.entity.Grid;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;

public interface GridRepository {

  List<Grid> findDistinctByStoreAndCategories(List<StoreId> storeIds, Set<CatGroupId> catGroupIds);

}
