package springfox.documentation.oas.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcBasePathAndHostnameTransformationFilter;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.OnServletBasedWebApplication;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static springfox.documentation.oas.web.SpecGeneration.OPEN_API_SPECIFICATION_PATH;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Conditional(OnServletBasedWebApplication.class)
@EnablePluginRegistries(WebMvcOpenApiTransformationFilter.class)
public class OpenApiWebMvcConfiguration {
    @Bean
    public WebMvcOpenApiTransformationFilter webMvcOpenApiTransformer(
            @Value(OPEN_API_SPECIFICATION_PATH) String oasPath) {
        return new WebMvcBasePathAndHostnameTransformationFilter(oasPath);
    }

    /**
     * 网关代理的时候，修复没有前缀的问题
     *
     * @return
     */
    @Bean
    public WebMvcOpenApiTransformationFilter webMvcOpenApiTransformationFilter() {
        return new WebMvcOpenApiTransformationFilter() {
            @Override
            public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
                OpenAPI specification = context.getSpecification();
                context.request().ifPresent(req -> {
                    String prefix = req.getHeader("X-Forwarded-Prefix");
                    if (Objects.nonNull(prefix)) {
                        Paths ps = specification.getPaths();
                        Map<String, PathItem> pathItemMap = specification.getPaths().entrySet().stream().collect(Collectors.toMap(item -> prefix + item.getKey(), Map.Entry::getValue));
                        Paths paths = new Paths();
                        paths.putAll(pathItemMap);
                        paths.setExtensions(ps.getExtensions());
                        specification.setPaths(paths);
                    }
                });
                return specification;
            }

            @Override
            public boolean supports(DocumentationType documentationType) {
                return true;
            }
        };
    }
}
