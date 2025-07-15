package com.academy.common.interceptor;

import com.academy.common.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TrackingIdInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingIdInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String trackingId = request.getHeader(CommonConstant.X_TRACKING_ID);
        if (StringUtils.isBlank(trackingId)){
            trackingId = java.util.UUID.randomUUID().toString();
            response.setHeader(CommonConstant.X_TRACKING_ID, trackingId);
        }
        request.setAttribute(CommonConstant.X_TRACKING_ID, trackingId);
        MDC.put(CommonConstant.X_TRACKING_ID, trackingId);

        return true;
    }
}
