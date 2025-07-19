package com.academy.common.interceptor;

import com.academy.common.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        String httpMethod = request.getMethod();
        if(StringUtils.equalsAnyIgnoreCase(httpMethod, RequestMethod.GET.name(), RequestMethod.DELETE.name())) {

            if (handler instanceof HandlerMethod handlerMethod) {
                String className = handlerMethod.getBeanType().getSimpleName();
                String methodName = handlerMethod.getMethod().getName();
                String fullMethod = className + "." + methodName;

                String requestURI = request.getRequestURI();

                StringBuilder paramLog = new StringBuilder();
                Enumeration<String> paramNames = request.getParameterNames();

                while (paramNames.hasMoreElements()) {
                    String param = paramNames.nextElement();
                    String value = request.getParameter(param);
                    if (param.toLowerCase().contains(CommonConstant.PASSWORD)) {
                        value = CommonConstant.DEFAULT_PASSWORD;
                    }
                    paramLog.append(param).append("=").append(value).append("&");
                }

                if (!paramLog.isEmpty()) {
                    paramLog.setLength(paramLog.length() - 1);
                }

                if (!paramLog.isEmpty()) {
                    LOGGER.debug("Before controller: [{} {}] {} - params: [{}]", httpMethod, requestURI, fullMethod, paramLog);
                } else {
                    LOGGER.debug("Before controller: [{} {}] {}", httpMethod, requestURI, fullMethod);
                }
            }
        }
        return true;
    }
}
