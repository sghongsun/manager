package me.sghong.manager.config;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.filter.HTMLTagFilter;
import me.sghong.manager.interceptor.AuthInterceptor;
import me.sghong.manager.resolver.PermissionResolver;
import me.sghong.manager.util.Constants;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final HTMLTagFilter htmlTagFilter;
    private final PermissionResolver permissionResolver;
    private final AuthInterceptor authInterceptor;

    @Bean
    public FilterRegistrationBean<HTMLTagFilter> getFilterRegistrationBean() {
        FilterRegistrationBean<HTMLTagFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(htmlTagFilter);
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*"); //전체 URL 포함
        //registrationBean.addUrlPatterns("/test/*"); //특정 URL 포함
        //registrationBean.setUrlPatterns(Arrays.asList("/manage/*", "/login/*"));
        return registrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**",
                        "/images/**",
                        "/js/**",
                        "/*.ico",
                        "/error",
                        "/login"
                );
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(permissionResolver);
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/SmartEditor/**").addResourceLocations("classpath:/static/SmartEditor/");
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///"+ Constants.uploadrootPath+"/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES));
    }

}
