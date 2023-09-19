package com.inditex.mecc.mectlglnk.usecase;

import java.util.List;
import java.util.function.Consumer;

import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
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

  private static Consumer<CatGroupId> sleepBeforeNextIteration() {
    return catGroupId -> {
      try {
        log.info("Sleeping for 500 ms");
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
  }

  @Override
  public void linkGrids(final Integer storeId) {
    StoreId store = StoreId.builder().value(storeId).build();
    List<CatGroupId> categories =
        this.getIopCategoriesBy(store)
            .stream()
            .filter(category -> this.isValidCategory(category, store))
            .peek(sleepBeforeNextIteration())
            .toList();

    this.saveIopCategoriesByStore(categories, store);
  }

  private List<CatGroupId> getIopCategoriesBy(StoreId storeId) {
    return gridRepository.findIopCategoriesByStoreId(storeId);
  }

  private boolean isValidCategory(CatGroupId catGroupId, StoreId storeId) {
    return gridRepository.isValidCategoryByStoreId(catGroupId, storeId);
  }

  private void saveIopCategoriesByStore(List<CatGroupId> categories, StoreId store) {
    gridCategoryGroupRepository.saveCategoriesByStoreId(categories, store);
  }

}
