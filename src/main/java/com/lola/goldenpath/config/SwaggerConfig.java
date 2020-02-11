package com.lola.goldenpath.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    private static final String SPRING_OAUTH = "SPRING_OAUTH";

    @Bean
    public Docket swaggerApi(/*final SecurityContext securityContext*/) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Goldenpath")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lola.goldenpath.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Goldenpath API Documentation")
                .description("Goldenpath application")
                .build();
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{};
    }

    @Bean
    public SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(
                        Collections.singletonList(new SecurityReference(SPRING_OAUTH, scopes())))
                .build();
    }
}
