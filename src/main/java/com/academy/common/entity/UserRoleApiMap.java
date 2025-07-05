package com.academy.common.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user_role_api_map")
public class UserRoleApiMap {

    private String id;
    private String role;
    private String path;
    private String method;
    private String description;

}
