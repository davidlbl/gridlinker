package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.inditex.mecc.mectlglnk.domain.entity.CatGroupId;
import com.inditex.mecc.mectlglnk.domain.entity.StoreId;
import com.inditex.mecc.mectlglnk.domain.repository.GridCategoryGroupRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
@RequiredArgsConstructor
public class FileGridCategoryGroupRepository implements GridCategoryGroupRepository {

  private final String outputFilePattern;

  @Override
  public void saveCategoriesByStoreId(List<CatGroupId> categories, StoreId storeId) {
    try {
      final List<String> categoryIdentifiers = categories.stream()
          .map(CatGroupId::getValue)
          .map(String::valueOf)
          .toList();

      final String pathname = String.format(this.outputFilePattern, storeId.getValue());

      File file = new File(pathname);
      this.addFileHeader(file);

      FileUtils.writeLines(file, categoryIdentifiers, true);
    } catch (final IOException e) {
      log.error("Error writing file", e);
    }
  }

  private void addFileHeader(File file) throws IOException {
    FileUtils.writeLines(file, List.of("category"), System.lineSeparator());
  }
}
