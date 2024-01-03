package com.samsung.framework.common.config;

import com.samsung.framework.common.interceptor.AuthorityInterceptor;
import com.samsung.framework.common.interceptor.LoggerInterceptor;
import com.samsung.framework.common.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SEC = 3600L;

    private final LoggerInterceptor loggerInterceptor;
    private final LoginInterceptor loginInterceptor;
    private final AuthorityInterceptor authorityInterceptor;

    // Logging
    private final List<String> loggerIncludePattern = Arrays.asList("/**");
    private final List<String> loggerExcludePattern = Arrays.asList("/error", "/resources/**", "/static/**");

    // Login
    private final List<String> loginIncludePattern = Arrays.asList("/login/**", "/member/**", "/token/authentication-info");
    private final List<String> loginExcludePattern = Arrays.asList("/login", "/member/sign-up");

    // Authority
    private final List<String> authorityIncludePattern = Arrays.asList("/auth/**", "/test/authorized-only");
    private final List<String> authorityExcludePattern = Arrays.asList("/login", "/member/**", "/test/**", "/error", "/jsp/**");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loggerInterceptor)
//                .addPathPatterns(loggerIncludePattern)
//                .excludePathPatterns(loggerExcludePattern)
//                .order(1);

//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns(loginIncludePattern)
//                .excludePathPatterns(loginExcludePattern)
//                .order(2);

//        registry.addInterceptor(authorityInterceptor)
//                .excludePathPatterns(authorityExcludePattern)
//                .order(3);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/")
//                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name())
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(MAX_AGE_SEC);
//    }

//    @Bean
//    public StrictHttpFirewall httpFirewall() {
//        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowedHttpMethods(Arrays.asList(
//                  HttpMethod.GET.name()
//                , HttpMethod.POST.name()
//                , HttpMethod.DELETE.name()
//                , HttpMethod.PUT.name()
//                , HttpMethod.PATCH.name())
//        );
//        return firewall;
//    }

}
