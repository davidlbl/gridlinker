package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.inditex.mecc.mectlglnk.domain.entity.GridCategoryGroup;
import com.inditex.mecc.mectlglnk.domain.repository.GridCategoryGroupRepository;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class FileGridCategoryGroupRepository implements GridCategoryGroupRepository {

  private final Logger logger;

  private final String outputFilePattern;

  @Override
  public void saveGridCategoryGroupList(final List<GridCategoryGroup> gridCategoryGroupList) {
    try {
      val sqlStatements = gridCategoryGroupList.stream().map(
          gridCategoryGroup -> this.toSqlStatement(JdbcQueryConstants.XCATGROUP_GRID_INSERT_STATEMENT, gridCategoryGroup))
          .toList();
      final String pathname = this.outputFilePattern;
      FileUtils.writeLines(new File(pathname), sqlStatements);
    } catch (final IOException e) {
      this.logger.error("Error writing sqlStatements", e);
    }
  }

  private String toSqlStatement(final String sqlStatement, final GridCategoryGroup gridCategoryGroup) {
    return String.format(
        sqlStatement,
        gridCategoryGroup.getStoreId().getValue(),
        gridCategoryGroup.getCatGroupId().getValue(),
        gridCategoryGroup.getGridCategory().getGridId().getValue(),
        gridCategoryGroup.getStatus().toString(),
        gridCategoryGroup.getCatalogId().getValue(),
        decorateSqlStringOrNull(gridCategoryGroup.getSettings()));
  }

  private static String decorateSqlStringOrNull(final String nullableValue) {
    return Optional.ofNullable(nullableValue).map(s -> "'".concat(s).concat("'")).orElse("null");
  }
}
