package com.academy.common.interceptor;

import com.academy.common.constant.UserRole;
import com.academy.common.repository.UserRoleApiMapRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import java.util.HashMap;
import java.util.Map;

@Component
public class RoleValidationInterceptor extends WebRequestHandlerInterceptorAdapter {

    private static final String ROLE_HEADER = "X-USER-ROLE";

    private static final Map<String, UserRole> METHOD_PATH_MAP = new HashMap<>();

    @Autowired
    private UserRoleApiMapRepository userRoleApiMapRepository;


    public RoleValidationInterceptor(WebRequestInterceptor requestInterceptor) {
        super(requestInterceptor);
    }


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
        String role = request.getHeader(ROLE_HEADER);
        if (StringUtils.isBlank(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Role header is missing");
            return false;
        }
        String uri = request.getRequestURI();
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
        return true;
    }

    private String getKey(String path, String method) {
        return path + ":" + method;
    }
}
