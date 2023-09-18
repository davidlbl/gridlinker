package com.inditex.mecc.mectlglnk.config;

import com.inditex.mecc.mectlglnk.domain.repository.GridCategoryGroupRepository;
import com.inditex.mecc.mectlglnk.infrastructure.repository.db2.FileGridCategoryGroupRepository;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfiguration {

  @Bean
  public GridCategoryGroupRepository db2DataSource(@Value("${amiga.data.file.output-pattern}") final String outputPattern) {
    return new FileGridCategoryGroupRepository(LoggerFactory.getLogger(FileGridCategoryGroupRepository.class), outputPattern);
  }

}
