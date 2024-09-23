package com.samsung.framework.common.config;

//import com.samsung.framework.common.interceptor.AuthorityInterceptor;
//import com.samsung.framework.common.interceptor.LoggerInterceptor;
//import com.samsung.framework.common.interceptor.LoginInterceptor;
import com.samsung.framework.common.interceptor.AuthInterceptor;
import com.samsung.framework.common.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SEC = 3600L;

    private final LoginInterceptor loginInterceptor;
    private final AuthInterceptor authInterceptor;

    // Static Path
//    private final List<String> staticExcludePattern = Arrays.asList("/static/js/**", "/static/lib/**/**", "/static/css/**", "/error", "/resources/**", "/static/**" ,"/menu/**", "/img/**", "/account/login");

    // Login
    private final List<String> loginIncludePattern = Arrays.asList("/login/**", "/member/**", "/token/authentication-info");
    private final List<String> loginExcludePattern = Arrays.asList("/static/js/**","/static/lib/**/**", "/static/css/**","/account/login", "/account/**","/error", "/resources/**", "/static/**" ,"/menu/**", "/img/**");

    // Authority
    private final List<String> authorityExcludePattern = Arrays.asList("/static/js/**", "/static/lib/**/**", "/static/css/**", "/error", "/resources/**", "/static/**" ,"/menu/**", "/img/**"
            , "/account/login", "/account/**","/account/logout"
            , "/account/api/**", "/account/myInfo/**"
            , "/contract/sign/wait/**", "/contract/sign/paper/comp/**"
            , "/account/pwdChange", "/contract/view"
            , "/file/download/**", "/contract/view/history"
            , "/pdf/download" , "/contract/create/**");

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3030") // 클라이언트 URL 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials") // 추가된 헤더 노출
                .allowCredentials(true); // 자격 증명 허용
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/")
                .addResourceLocations("C:/files/electronicContract/img/logo/")
                .addResourceLocations("/files/electronicContract/img/logo/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(loginExcludePattern)
                .order(1);

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(authorityExcludePattern)
                .order(2);
    }

}
