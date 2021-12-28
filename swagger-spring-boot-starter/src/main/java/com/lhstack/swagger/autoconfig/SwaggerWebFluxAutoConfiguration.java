package com.lhstack.swagger.autoconfig;

import com.lhstack.swagger.filters.BasicFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * @author lhstack
 * @date 2021/9/30
 * @class SwaggerWebFluxAutoConfiguration
 * @since 1.8
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class SwaggerWebFluxAutoConfiguration {


    @Bean
    public WebFluxConfigurer webFluxConfigurer() {
        return new WebFluxConfigurer() {
            @Override
            public void addResourceHandlers(org.springframework.web.reactive.config.ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/")
                        .addResourceLocations("classpath:/META-INF/resources/")
                        .setCacheControl(CacheControl.maxAge(Duration.ofDays(7)));
            }
        };
    }

    @Bean
    @ConditionalOnProperty(
            name = {"knife4j.basic.enable"},
            havingValue = "true"
    )
    public WebFilter securityBasicAuthFilter(
            @Value("${knife4j.basic.username:admin}") String username,
            @Value("${knife4j.basic.password:123456}") String password,
            @Value("${knife4j.basic.enable:false}") Boolean enableBasicAuth
    ) {

        class SecurityBasicAuthWebFilter extends BasicFilter implements WebFilter {

            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                return exchange.getSession().flatMap(session -> {
                    if (enableBasicAuth) {
                        if (this.match(request.getPath().toString())) {
                            Object swaggerSessionValue = session.getAttribute("SwaggerBootstrapUiBasicAuthSession");
                            if (swaggerSessionValue != null) {
                                return chain.filter(exchange);
                            } else {
                                String auth = request.getHeaders().getFirst("Authorization");
                                if (auth != null && !"".equals(auth)) {
                                    String userAndPass = this.decodeBase64(auth.substring(6));
                                    String[] upArr = userAndPass.split(":");
                                    if (upArr.length != 2) {
                                        return writeForbiddenCode(exchange);
                                    }
                                    String iptUser = upArr[0];
                                    String iptPass = upArr[1];
                                    if (iptUser.equals(username) && iptPass.equals(password)) {
                                        session.getAttributes().put("SwaggerBootstrapUiBasicAuthSession", username);
                                        return chain.filter(exchange);
                                    }
                                    return writeForbiddenCode(exchange);
                                }
                                return writeForbiddenCode(exchange);
                            }
                        }
                    }
                    return chain.filter(exchange);
                });
            }

            private Mono<? extends Void> writeForbiddenCode(ServerWebExchange exchange) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add("WWW-Authenticate", "Basic realm=\"input Swagger Basic userName & password \"");
                DataBufferFactory dataBufferFactory = response.bufferFactory();
                return response.writeWith(Mono.just(dataBufferFactory.wrap("You do not have permission to access this resource".getBytes(StandardCharsets.UTF_8))));
            }
        }
        return new SecurityBasicAuthWebFilter();
    }

}
