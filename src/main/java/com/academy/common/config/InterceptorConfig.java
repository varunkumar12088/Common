package com.academy.common.config;

import com.academy.common.interceptor.RequestLoggingInterceptor;
import com.academy.common.interceptor.RoleValidationInterceptor;
import com.academy.common.interceptor.TrackingIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private TrackingIdInterceptor trackingIdInterceptor;
    @Autowired
    private RoleValidationInterceptor roleValidationInterceptor;

    @Autowired
    private RequestLoggingInterceptor requestLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Register the TrackingIdInterceptor
        registry.addInterceptor(trackingIdInterceptor)
                .addPathPatterns("/**");

        // Register the requestLoggingInterceptor
        registry.addInterceptor(requestLoggingInterceptor)
                        .addPathPatterns("/**");

        // Register the RoleValidationInterceptor
        registry.addInterceptor(roleValidationInterceptor)
                .addPathPatterns("/**")  // Apply to all paths
                .excludePathPatterns("/public/**", "/error/**");
    }
}
