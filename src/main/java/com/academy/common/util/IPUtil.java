package com.academy.common.util;

import com.academy.common.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPUtil.class);

    private static final String COMMA = ",";

    public static String getClientIpAddress(HttpServletRequest request) {
        LOGGER.debug("Extracting client IP address from request headers.");

        String ip = request.getHeader(CommonConstant.X_FORWARDED_FOR);
        if (StringUtils.isBlank(ip) || CommonConstant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(CommonConstant.PROXY_CLIENT_IP);
        }

        if (StringUtils.isBlank(ip) || CommonConstant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(CommonConstant.WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || CommonConstant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(CommonConstant.HTTP_X_FORWARDED_FOR);
        }
        if (StringUtils.isBlank(ip) || CommonConstant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotBlank(ip) && ip.contains(COMMA)) {
            ip = ip.split(COMMA)[0].trim();
        }


        LOGGER.info("Extracted client IP address: {}", ip);
        return ip;
    }
}
