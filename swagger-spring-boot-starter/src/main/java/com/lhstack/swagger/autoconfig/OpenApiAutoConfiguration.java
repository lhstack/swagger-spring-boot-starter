package com.lhstack.swagger.autoconfig;

import com.lhstack.swagger.properties.SwaggerProperties;
import io.swagger.annotations.Api;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lhstack
 * @date 2021/9/28
 * @class SwaggerConfiguration
 * @since 1.8
 */
@ConditionalOnProperty(value = "knife4j.enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
@AutoConfigureOrder(Integer.MIN_VALUE)
@EnableOpenApi
public class OpenApiAutoConfiguration implements BeanFactoryAware {

    @Autowired
    private SwaggerProperties swaggerProperties;

    /**
     * 注册文档
     *
     * @param properties
     * @param beanFactory
     */
    private void registerDockets(Set<SwaggerProperties.Properties> properties, DefaultListableBeanFactory beanFactory) {
        for (SwaggerProperties.Properties property : properties) {
            registerDocket(property, beanFactory);
        }
    }

    /**
     * 单个文档注册
     *
     * @param property
     * @param beanFactory
     */
    private void registerDocket(SwaggerProperties.Properties property, DefaultListableBeanFactory beanFactory) {
        Set<SwaggerProperties.Parameter> parameters = new HashSet<>(this.swaggerProperties.getParameters());
        parameters.addAll(property.getParameters());
        Docket docket = new Docket(property.getDocumentType()
                .getDocumentationType())
                .enable(property.getEnable())
                .groupName(property.getGroup())
                .ignoredParameterTypes(property.getIgnoredParameterTypes().toArray(new Class[0]))
                .globalRequestParameters(parameters.stream().map(SwaggerProperties.Parameter::toRequestParameter).collect(Collectors.toList()))
                .apiInfo(new ApiInfoBuilder()
                        .title(property.getTitle())
                        .description(property.getDescription())
                        .version(property.getVersion())
                        .contact(new Contact(property.getContactName(), property.getContactUrl(), property.getContactEmail()))
                        .license(property.getLicense())
                        .licenseUrl(property.getLicenseUrl())
                        .termsOfServiceUrl(property.getTermsOfServiceUrl())
                        .build())
                .select()
                .apis(StringUtils.hasText(property.getBasePackage()) ? RequestHandlerSelectors.basePackage(property.getBasePackage())
                        .and(RequestHandlerSelectors.withClassAnnotation(Api.class)) : RequestHandlerSelectors.any()
                        .and(RequestHandlerSelectors.withClassAnnotation(Api.class))).build();

        if (docket.isEnabled()) {
            beanFactory.registerSingleton(docket.getClass().getName() + "-" + docket.getGroupName(), docket);
        }
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (Objects.nonNull(this.swaggerProperties)) {
            Set<SwaggerProperties.Properties> properties = new HashSet<>();
            if (Objects.nonNull(this.swaggerProperties.getDefaultProperties())) {
                properties.add(this.swaggerProperties.getDefaultProperties());
            }
            if (!CollectionUtils.isEmpty(this.swaggerProperties.getProperties())) {
                properties.addAll(this.swaggerProperties.getProperties());
            }
            registerDockets(properties, (DefaultListableBeanFactory) beanFactory);
        }
    }
}
