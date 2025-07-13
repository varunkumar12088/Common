package com.academy.common.service;


import com.academy.common.dto.UserRoleApiSkipDto;

public interface UserRoleApiSkipService {

    void addUserRoleApiSkip(UserRoleApiSkipDto apiSkipDto);

    void removeUserRoleApiSkip(String uri, String method);

    UserRoleApiSkipDto getUserRoleApiSkip(String uri, String method);

    boolean isApiSkipped(String uri, String method);
}
