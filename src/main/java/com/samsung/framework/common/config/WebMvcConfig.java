package com.samsung.framework.common.config;

//import com.samsung.framework.common.interceptor.AuthorityInterceptor;
//import com.samsung.framework.common.interceptor.LoggerInterceptor;
//import com.samsung.framework.common.interceptor.LoginInterceptor;
import com.samsung.framework.common.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SEC = 3600L;

    private final LoginInterceptor loginInterceptor;


    // Login
    private final List<String> loginIncludePattern = Arrays.asList("/login/**", "/member/**", "/token/authentication-info");
    private final List<String> loginExcludePattern = Arrays.asList("/account/login", "/error", "/resources/**", "/static/**" ,"/menu/**", "/img/**");


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(loginExcludePattern)
//                .order(1);
//
//
//    }

}
