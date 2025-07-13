package com.academy.common.service.impl;

import com.academy.common.dto.AllowedIPDto;
import com.academy.common.entity.AllowedIP;
import com.academy.common.repository.AllowedIPRepository;
import com.academy.common.service.AllowedIPService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllowedIPServiceImpl implements AllowedIPService {

    private final Logger logger = LoggerFactory.getLogger(AllowedIPServiceImpl.class);

    @Autowired
    private AllowedIPRepository allowedIPRepository;

    private static final List<String> ALLOWED_IPS = new ArrayList<>();

    @PostConstruct
    public void init(){
        allowedIPRepository.findAll().stream().map(AllowedIP::getIpAddress).forEach(ip -> {
            ALLOWED_IPS.add(ip);
        });
    }
    @Override
    public void addAllowedIP(AllowedIPDto allowedIPDto) {
        logger.debug("Adding allowed IP: {}", allowedIPDto.getIpAddress());
        AllowedIP allowedIP = allowedIPDto.toAllowedIP();
        allowedIPRepository.save(allowedIP);
    }

    @Override
    public void removeAllowedIP(String ipAddress) {
        logger.debug("Removing allowed IP: {}", ipAddress);
        if (allowedIPRepository.existsByIpAddress(ipAddress)) {
            AllowedIP allowedIP = allowedIPRepository.findByIpAddress(ipAddress);
            allowedIPRepository.delete(allowedIP);
        } else {
            logger.warn("IP address {} not found in allowed IPs", ipAddress);
        }
    }

    @Override
    public boolean isIPAllowed(String ipAddress) {
        logger.debug("Checking if IP is allowed: {}", ipAddress);
        if(StringUtils.isNotBlank(ipAddress) && ALLOWED_IPS.contains(ipAddress)) {
            logger.info("IP address {} is allowed", ipAddress);
            return true;
        }
        return allowedIPRepository.existsByIpAddress(ipAddress);
    }

    @Override
    public void clearAllowedIPs() {
        logger.debug("Clearing all allowed IPs");
        allowedIPRepository.deleteAll();
        logger.info("All allowed IPs have been cleared");
    }

    @Override
    public List<String> getAllAllowedIPs() {
        logger.debug("Fetching all allowed IPs");
        List<AllowedIP> allowedIPs = allowedIPRepository.findAll();
        if (CollectionUtils.isEmpty(allowedIPs)) {
           return new ArrayList<>();
        }
        return allowedIPs.stream()
                .map(AllowedIP::getIpAddress)
                .toList();
    }
}
