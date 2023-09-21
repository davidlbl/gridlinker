package com.inditex.mecc.mectlglnk.usecase;

import java.util.List;
import java.util.function.Consumer;

import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;
import com.inditex.mecc.mectlglnk.domain.repository.CategoryRepository;
import com.inditex.mecc.mectlglnk.domain.repository.GridCategoryGroupRepository;
import com.inditex.mecc.mectlglnk.domain.usecase.LinkGridsUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkGridsUseCaseImpl implements LinkGridsUseCase {

  private final CategoryRepository gridRepository;

  private final GridCategoryGroupRepository gridCategoryGroupRepository;

  @Override
  public void linkGrids(final Integer storeId) {
    StoreId store = StoreId.builder().value(storeId).build();
    List<CatGroupId> categories =
        this.getIopGridCategoryGroupBy(store)
            .stream()
            .filter(this::isValidGrid)
            .map(GridCategoryGroup::getCatGroupId)
            .toList();

    this.saveIopCategoriesByStore(categories, store);
  }

  private boolean isValidGrid(GridCategoryGroup gridCategoryGroup) {
    return gridRepository.existGridBy(gridCategoryGroup.getGridCategory().getGridId(), gridCategoryGroup.getStoreId());
  }


  private List<GridCategoryGroup> getIopGridCategoryGroupBy(StoreId storeId) {
    return gridRepository.findIopCategoriesByStoreId(storeId);
  }


  private void saveIopCategoriesByStore(List<CatGroupId> categories, StoreId store) {
    gridCategoryGroupRepository.saveCategoriesByStoreId(categories, store);
  }

}
