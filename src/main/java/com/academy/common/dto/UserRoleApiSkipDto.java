package com.academy.common.dto;

import com.academy.common.entity.UserRoleApiSkip;
import lombok.Data;

@Data
public class UserRoleApiSkipDto {

    private String uri;
    private String method;
    private String skipReason;

    public UserRoleApiSkip toUserRoleApiSkip() {
        return UserRoleApiSkip.builder()
                .uri(this.uri)
                .method(this.method)
                .skipReason(this.skipReason)
                .build();
    }
}
