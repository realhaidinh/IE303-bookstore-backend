package com.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Genre;
import com.bookstore.model.Order;

/**
 * Cấu hình Rest API
 */
@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {
  /**
   * Cho phép trả về id của document trong database
   * @return
   */
  @Bean
  public RepositoryRestConfigurer repositoryRestConfigurer() {
    return RepositoryRestConfigurer.withConfig(config -> {
      config.exposeIdsFor(Book.class);
      config.exposeIdsFor(Author.class);
      config.exposeIdsFor(Genre.class);
      config.exposeIdsFor(Order.class);
    });
  }
}
