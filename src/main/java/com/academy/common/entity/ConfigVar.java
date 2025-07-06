package com.academy.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config_vars")
@AllArgsConstructor
@Data
public class ConfigVar {

    private String id;
    private Object value;
    private boolean encrypted;
}
