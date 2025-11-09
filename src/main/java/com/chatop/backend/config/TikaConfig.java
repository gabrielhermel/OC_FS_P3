package com.chatop.backend.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exposes Apache Tika as a bean for content detection.
 */
@Configuration
public class TikaConfig {

  @Bean
  public Tika tika() {
    return new Tika();
  }

}
