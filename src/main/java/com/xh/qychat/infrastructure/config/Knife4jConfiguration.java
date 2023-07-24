package com.xh.qychat.infrastructure.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Title:
 * Description:
 *
 * @author H.Yang
 * @date 2020/12/18
 */
@Configuration
@EnableOpenApi
@Import(BeanValidatorPluginsConfiguration.class)
public class Knife4jConfiguration {

    /**
     * 引入Knife4j提供的扩展类
     */
    private OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public Knife4jConfiguration(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean(value = "defaultApi")
    @Order(value = 1)
    public Docket defaultApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(getApiInfo())
                .groupName("会话存档")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xh.qychat.controller.facade"))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildSettingExtensions());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("企业微信会话存档中心(QYCHAT-CENTER)")
                .description("<div style='font-size:14px;color:red;'>QYCHAT-CENTER REST FUL API</div>")
                .termsOfServiceUrl("https://www.anchnet.com/")
                .contact(new Contact("IDC研发部", "https://www.anchnet.com/", "changhy@anchnet.com"))
                .version("1.0")
                .build();
    }

}
