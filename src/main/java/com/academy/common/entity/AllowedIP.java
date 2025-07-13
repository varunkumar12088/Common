package com.academy.common.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "allowed_ips")
@Data
@Builder
public class AllowedIP {

    private String id;
    private String ipAddress;
    private String description;
}
