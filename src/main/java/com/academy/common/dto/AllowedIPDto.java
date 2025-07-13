package com.academy.common.dto;

import com.academy.common.entity.AllowedIP;
import lombok.Data;

@Data
public class AllowedIPDto {

    private String ipAddress;
    private String description;

    public AllowedIP toAllowedIP() {
        AllowedIP allowedIP = AllowedIP.builder()
                .ipAddress(this.ipAddress)
                .description(this.description)
                .build();
        return allowedIP;
    }
}
