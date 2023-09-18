package com.inditex.mecc.mectlglnk.usecase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
import com.inditex.mecc.mectlglnk.domain.entity.CatalogId;
import com.inditex.mecc.mectlglnk.domain.entity.Grid;
import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;
import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup.GridStatus;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;
import com.inditex.mecc.mectlglnk.domain.repository.GridCategoryGroupRepository;
import com.inditex.mecc.mectlglnk.domain.repository.GridRepository;
import com.inditex.mecc.mectlglnk.domain.usecase.LinkGridsUseCase;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkGridsUseCaseImpl implements LinkGridsUseCase {

  public static final int DEFAULT_VALUE = 0;

  private final Logger logger;

  private final GridCategoryGroupRepository gridCategoryGroupRepository;

  private final GridRepository gridRepository;

  private final Random random = new Random();

  private static Set<CatGroupId> iopCategories(List<Grid> gridIds) {
    return gridIds.stream()
        .map(Grid::getCatalogId)
        .collect(Collectors.toSet());
  }

  @Override
  public void linkGrids(final List<Integer> storeIds) {

    Set<CatGroupId> categories = this.loadCategoriesFromFile();

    List<StoreId> stores = storeIds.stream().map(storeId -> StoreId.builder().value(storeId).build()).toList();

    List<Grid> iopGrids = this.getGridFromStoresAndCategories(stores, categories);

    categories.removeAll(iopCategories(iopGrids));

    var gridCategoryGroupList = this.buildRandonGridsFromLegacyCategories(categories, stores, iopGrids);

    this.gridCategoryGroupRepository.saveGridCategoryGroupList(gridCategoryGroupList);

    }

  private List<GridCategoryGroup> buildRandonGridsFromLegacyCategories(Set<CatGroupId> categories, List<StoreId> stores, List<Grid> iopGridCategories) {

    List<GridCategoryGroup> gridCategoryGroups = new ArrayList<>();

    categories.forEach(category -> {

    Grid randomGridId = this.getRandomGridId(iopGridCategories);
    StoreId randomStoreId = this.getRandomStoreId(stores);

    Grid gridCategory = Grid.builder().gridId(randomGridId.getGridId()).catalogId(randomGridId.getCatalogId()).build();
    CatalogId catalogId = CatalogId.builder().value(DEFAULT_VALUE).build();

      GridCategoryGroup gridCategoryGroup = GridCategoryGroup.builder()
          .storeId(randomStoreId)
          .catalogId(catalogId)
          .gridCategory(gridCategory)
          .status(GridStatus.LIVE)
          .catGroupId(category)
          .settings(null)
          .build();

      gridCategoryGroups.add(gridCategoryGroup);
    });

    return gridCategoryGroups;
  }

  private List<Grid> getGridFromStoresAndCategories(List<StoreId> storeIds, Set<CatGroupId> categories) {
    return gridRepository.findDistinctByStoreAndCategories(storeIds, categories);
  }

  private Set<CatGroupId> loadCategoriesFromFile() {
    try {
      File[] files = Paths.get(this.getClass().getResource("/categories/").toURI())
          .toFile().listFiles();

      return Arrays.stream(files).filter(File::isFile)
          .map(this::readFileContent)
          .flatMap(Collection::stream)
          .collect(Collectors.toSet());
    } catch (Exception e) {
      return Collections.emptySet();
    }
  }

  private Set<CatGroupId> readFileContent(File file) {
    try {
      return Files.readAllLines(file.toPath()).stream()
              .filter(StringUtils::isNoneBlank)
              .map(line -> line.split(",")[0])
              .map(Integer::parseInt)
              .map(categoryId -> CatGroupId.builder().value(categoryId).build())
              .collect(Collectors.toSet());
    } catch (IOException e) {
      return Collections.emptySet();
    }
  }

  private Grid getRandomGridId(final List<Grid> gridIds) {
    return gridIds.get(this.random.nextInt(0, gridIds.size()));
  }

  private StoreId getRandomStoreId(List<StoreId> stores) {
    return stores.get(this.random.nextInt(0, stores.size()));
  }

}
