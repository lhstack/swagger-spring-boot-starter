package com.lhstack.swagger.autoconfig;

import com.lhstack.swagger.filters.BasicFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

/**
 * @author lhstack
 * @date 2021/9/30
 * @class SwaggerWebMvcAutoConfiguration
 * @since 1.8
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SwaggerWebMvcAutoConfiguration {
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("doc.html")
                        .addResourceLocations("classpath:/META-INF/resources/")
                        .setCacheControl(CacheControl.maxAge(Duration.ofDays(7)));
                registry.addResourceHandler("/webjars/**")
                        .addResourceLocations("classpath:/META-INF/resources/webjars/")
                        .setCacheControl(CacheControl.maxAge(Duration.ofDays(7)));
            }
        };
    }

    @Bean({"knife4jCorsFilter"})
    @ConditionalOnMissingBean({CorsFilter.class})
    @ConditionalOnProperty(
            name = {"knife4j.cors"},
            havingValue = "true"
    )
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(10000L);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Bean
    @ConditionalOnProperty(
            name = {"knife4j.basic.enable"},
            havingValue = "true"
    )
    public Filter securityBasicAuthFilter(
            @Value("${knife4j.basic.username:admin}") String username,
            @Value("${knife4j.basic.password:123456}") String password,
            @Value("${knife4j.basic.enable:false}") Boolean enableBasicAuth
    ) {

        class SecurityBasicAuthFilter extends BasicFilter implements Filter {
            private void writeForbiddenCode(HttpServletResponse httpServletResponse) throws IOException {
                httpServletResponse.setStatus(401);
                httpServletResponse.setHeader("WWW-Authenticate", "Basic realm=\"input Swagger Basic userName & password \"");
                httpServletResponse.getWriter().write("You do not have permission to access this resource");
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                HttpServletRequest servletRequest = (HttpServletRequest) request;
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                if (enableBasicAuth) {
                    if (this.match(servletRequest.getRequestURI())) {
                        Object swaggerSessionValue = servletRequest.getSession().getAttribute("SwaggerBootstrapUiBasicAuthSession");
                        if (swaggerSessionValue != null) {
                            chain.doFilter(request, response);
                        } else {
                            String auth = servletRequest.getHeader("Authorization");
                            if (auth != null && !"".equals(auth)) {
                                String userAndPass = this.decodeBase64(auth.substring(6));
                                String[] upArr = userAndPass.split(":");
                                if (upArr.length != 2) {
                                    this.writeForbiddenCode(httpServletResponse);
                                    return;
                                }
                                String iptUser = upArr[0];
                                String iptPass = upArr[1];
                                if (iptUser.equals(username) && iptPass.equals(password)) {
                                    servletRequest.getSession().setAttribute("SwaggerBootstrapUiBasicAuthSession", username);
                                    chain.doFilter(request, response);
                                    return;
                                }
                                this.writeForbiddenCode(httpServletResponse);
                                return;
                            }
                            this.writeForbiddenCode(httpServletResponse);
                        }
                    } else {
                        chain.doFilter(request, response);
                    }
                } else {
                    chain.doFilter(request, response);
                }
            }
        }
        return new SecurityBasicAuthFilter();
    }
}
