package com.academy.common.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Event {

    private String type;
    private Object data;
    private Map<String, String> headers;

}
