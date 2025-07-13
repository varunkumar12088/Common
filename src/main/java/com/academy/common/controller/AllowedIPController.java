package com.academy.common.controller;

import com.academy.common.dto.AllowedIPDto;
import com.academy.common.service.AllowedIPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/common/allowed-ips")
public class AllowedIPController {

    private static final Logger logger = LoggerFactory.getLogger(AllowedIPController.class);

    @Autowired
    private AllowedIPService allowedIPService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addAllowedIP(@RequestBody AllowedIPDto allowedIPDto) {
        logger.info("Adding allowed IP: {}", allowedIPDto.getIpAddress());
           // Validate the IP address format
        allowedIPService.addAllowedIP(allowedIPDto);
        return ResponseEntity.ok("IP address added successfully");
    }
}
