package com.inditex.mecc.mectlglnk.config;

import com.inditex.aqsw.framework.data.jdbc.datasources.DataSourceBuilder;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class JdbcConfiguration {

  @Bean(name = "wcs-db")
  @ConfigurationProperties(prefix = "amiga.data.jdbc.datasource.wcs-db")
  public DataSource db2DataSource(final DataSourceBuilder dataSourceBuilder) {
    return dataSourceBuilder.build();
  }

  @Bean
  @Primary
  public NamedParameterJdbcOperations uezaraJdbcTemplate(@Qualifier("wcs-db") final DataSource datasource) {
    return new NamedParameterJdbcTemplate(datasource);
  }

}
