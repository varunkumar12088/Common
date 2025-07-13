package com.academy.common.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_role_api_skip")
@Data
@Builder
public class UserRoleApiSkip {

    private String id;
    private String uri;
    private String method;
    private String skipReason;
}
