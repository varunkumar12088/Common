package com.academy.common.interceptor;

import com.academy.common.constant.CommonConstant;
import com.academy.common.constant.UserRole;
import com.academy.common.entity.UserRoleApiMap;
import com.academy.common.repository.UserRoleApiMapRepository;
import com.academy.common.service.AllowedIPService;
import com.academy.common.service.UserRoleApiSkipService;
import com.academy.common.util.IPUtil;
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


    private static final Map<String, UserRoleApiMap> METHOD_PATH_MAP = new HashMap<>();

    @Autowired
    private UserRoleApiMapRepository userRoleApiMapRepository;

    @Autowired
    private UserRoleApiSkipService userRoleApiSkipService;

    @Autowired
    private AllowedIPService allowedIPService;

    @PostConstruct
    public void init() {
        // Load the user role API map from the repository
        userRoleApiMapRepository.findAll().forEach(mapping -> {
            String key = getKey(mapping.getPath(), mapping.getMethod());
            METHOD_PATH_MAP.put(key, mapping);
        });
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.debug("RoleValidationInterceptor preHandle called for request: {}", request.getRequestURI());
        String uri = request.getRequestURI();
        String method = request.getMethod();
        LOGGER.debug("Checking access for URI: {}, Method: {}", uri, method);
        if(userRoleApiSkipService.isApiSkipped(uri, request.getMethod())){
            return true;
        }
        String role = request.getHeader(CommonConstant.ROLE_HEADER);
        if (StringUtils.isBlank(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Role header is missing");
            return false;
        }

        String key = getKey(uri, method);
        UserRoleApiMap userRoleApiMap = METHOD_PATH_MAP.get(key);

        // If no mapping found, check if the role is INTERNAL or ADMIN
        if (ObjectUtils.isEmpty(userRoleApiMap) && StringUtils.equalsAnyIgnoreCase(role, UserRole.INTERNAL.name(), UserRole.ADMIN.name())) {
            return true;
        }
        // If mapping found, check if the role has access
        if (!UserRole.from(role).hasAccessTo(UserRole.from(userRoleApiMap.getRole()))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return false;
        }
        // Check if the IP is allowed
        String clientIp = IPUtil.getClientIpAddress(request);
        if (!userRoleApiMap.isPublicApi() && !allowedIPService.isIPAllowed(clientIp)) {
            LOGGER.warn("Access denied for IP: {}", clientIp);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Your IP address is not allowed to access this resource");
            return false;
        }
        LOGGER.debug("Role {} has access to path: {}, method: {}", role, uri, method);
        return true;
    }

    private String getKey(String path, String method) {
        return path + ":" + method;
    }
}
