package com.academy.common.service;

import com.academy.common.dto.AllowedIPDto;

import java.util.List;

public interface AllowedIPService {

    void addAllowedIP(AllowedIPDto allowedIPDto);
    void removeAllowedIP(String ipAddress);
    boolean isIPAllowed(String ipAddress);
    void clearAllowedIPs();
    List<String> getAllAllowedIPs();
}
