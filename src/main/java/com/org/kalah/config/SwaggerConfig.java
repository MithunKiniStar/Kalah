package com.org.kalah.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * API documentation configuration of Kalah app.
 *
 * Created by Mithun Kini on 07/Feb/2020.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Mithun Kini", null, "kinimithun@gmail.com");
        return new ApiInfoBuilder()
                .title("Kalah Game")
                .description("Java RESTful Web Service that runs a game of 6-stone Kalah")
                .contact(contact)
                .version("1.0.0")
                .build();

    }
}
