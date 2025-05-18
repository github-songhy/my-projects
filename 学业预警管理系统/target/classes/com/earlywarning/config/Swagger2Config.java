package com.earlywarning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration //标记配置类
@EnableSwagger2 //开启在线接口文档
public class Swagger2Config {

    /**
     * 添加摘要信息(Docket)
     */
    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("标题：系统_接口文档")
                        .description("描述：用于管理公司的客户信息,具体包括XXX,XXX模块...")
                        .contact(new Contact("User", null, null))
                        .version("版本号:1.0")
                        .termsOfServiceUrl("127.0.0.1/earlywarning(/")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.earlywarning.controller"))
                .paths(PathSelectors.any())
                .build();
    }


}
