package com.inditex.mecc.mectlglnk.config;

import com.inditex.mecc.mectlglnk.domain.repository.GridCategoryGroupRepository;
import com.inditex.mecc.mectlglnk.infrastructure.repository.db2.FileGridCategoryGroupRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfiguration {

  @Bean
  public GridCategoryGroupRepository gridCategoryGroupRepository(@Value("${amiga.data.file.save-pattern}") final String outputPattern) {
    return new FileGridCategoryGroupRepository(outputPattern);
  }

}
