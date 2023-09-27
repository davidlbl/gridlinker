package com.inditex.mecc.mectlglnk.config;

import com.inditex.aqsw.framework.common.rest.client.RestClient;
import com.inditex.aqsw.framework.common.rest.client.builder.RestClientBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RestClientConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "amiga.common.rest.client.gridcore-client")
  public RestClient gridCoreRestClient(final RestClientBuilder builder) {
    return builder.build();
  }

  @Bean
  @ConfigurationProperties(prefix = "amiga.common.rest.client.navigation-client")
  public RestClient navigationRestClient(final RestClientBuilder builder) {
    return builder.build();
  }
}
