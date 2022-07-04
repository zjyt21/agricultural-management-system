package com.hlp.agrisys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author Mr.Han
 * @create 2022-06-23 21:22
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        /**
         * Create API application
         * apiInfo() Add API related information
         * Returns an ApiSelectorBuilder instance through the select() function to control which interfaces are exposed to Swagger for display,
         * This example uses the specified scan package path to define the specified directory to build the API.
         *
         * @return
         */
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Standard interface")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(true)
                .forCodeGeneration(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hlp.agrisys.controller"))//Configure the control plane location
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Basic information for creating the API (these basic information will be displayed on the documentation page)
     * Access address: http://ip:port/swagger-ui/index.html#/
     *
     */
    private ApiInfo apiInfo() {
        // author information
        Contact contact = new Contact("hlp","https://gitee.com/zjyt", "1103953171@qq.com");
        return new ApiInfo(
                "SwaggerAPI documentation of smart farming project",
                "API of intelligent agriculture background management system",
                "1.0.0",
                "https://gitee.com/zjyt",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
