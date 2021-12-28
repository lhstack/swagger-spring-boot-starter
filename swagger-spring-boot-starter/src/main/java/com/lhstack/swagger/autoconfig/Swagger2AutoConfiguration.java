package com.lhstack.swagger.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lhstack
 * @date 2021/9/30
 * @class Swagger2AutoConfiguration
 * @since 1.8
 */
@ConditionalOnProperty(value = "knife4j.enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(name = {"springfox.documentation.swagger2.web.Swagger2ControllerWebFlux","springfox.documentation.swagger2.web.Swagger2ControllerWebMvc"})
@EnableSwagger2
public class Swagger2AutoConfiguration {


}
