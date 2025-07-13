package com.academy.common.interceptor;

import com.academy.common.constant.CommonConstant;
import com.academy.common.constant.UserRole;
import com.academy.common.repository.UserRoleApiMapRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class RoleValidationInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleValidationInterceptor.class);;


    private static final Map<String, UserRole> METHOD_PATH_MAP = new HashMap<>();

    @Autowired
    private UserRoleApiMapRepository userRoleApiMapRepository;


    @PostConstruct
    public void init() {
        // Load the user role API map from the repository
        userRoleApiMapRepository.findAll().forEach(mapping -> {
            String key = getKey(mapping.getPath(), mapping.getMethod());
            METHOD_PATH_MAP.put(key, UserRole.from(mapping.getRole()));
        });
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.debug("RoleValidationInterceptor preHandle called for request: {}", request.getRequestURI());
        String uri = request.getRequestURI();
        if(StringUtils.containsAny(uri, "login", "register", "registration", "forgot-password", "reset-password", "verify-email", "resend-email-verification")){
            return true;
        }
        String role = request.getHeader(CommonConstant.ROLE_HEADER);
        if (StringUtils.isBlank(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Role header is missing");
            return false;
        }
        String method = request.getMethod();
        String key = getKey(uri, method);
        UserRole requiredRole = METHOD_PATH_MAP.get(key);
        if (ObjectUtils.isEmpty(requiredRole)) {
            // If no specific role is required for this path and method, allow access
            return true;
        }
        if (!UserRole.from(role).hasAccessTo(requiredRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return false;
        }
        LOGGER.debug("Role {} has access to path: {}, method: {}", role, uri, method);
        return true;
    }

    private String getKey(String path, String method) {
        return path + ":" + method;
    }
}
