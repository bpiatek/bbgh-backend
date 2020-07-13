package com.github.bpiatek.bbghbackend.swagger;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Bartosz Piatek on 13/07/2020
 */
@EnableSwagger2
@Configuration
class SwaggerConfiguration {
  @Bean
  Docket swaggerApi() {
    return new Docket(SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.github.bpiatek.bbghbackend.controller"))
        .paths(PathSelectors.any())
        .build();
  }


  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("BBGH-backend")
        .description("Backend of the application to retrieve information about footballers from specified portals")
        .version("0.0.1")
        .build();
  }
}
