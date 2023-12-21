package com.samsung.framework.common.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LayOutConfig {
    // thymeleaf layout
    @Bean
    public LayoutDialect layOutDialect() {
        return new LayoutDialect();
    }
}
