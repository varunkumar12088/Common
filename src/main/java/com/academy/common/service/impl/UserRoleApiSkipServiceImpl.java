package com.academy.common.service.impl;

import com.academy.common.dto.UserRoleApiSkipDto;
import com.academy.common.entity.UserRoleApiSkip;
import com.academy.common.repository.UserRoleApiSkipRepository;
import com.academy.common.service.UserRoleApiSkipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleApiSkipServiceImpl implements UserRoleApiSkipService {

    //LOgger
     private static final Logger logger = LoggerFactory.getLogger(UserRoleApiSkipServiceImpl.class);
     @Autowired
     private UserRoleApiSkipRepository userRoleApiSkipRepository;

    @Override
    public void addUserRoleApiSkip(UserRoleApiSkipDto apiSkipDto) {
        logger.info("Adding user role API skip for URI: {}, Method: {}", apiSkipDto.getUri(), apiSkipDto.getMethod());
        UserRoleApiSkip userRoleApiSkip = apiSkipDto.toUserRoleApiSkip();
        userRoleApiSkipRepository.save(userRoleApiSkip);
    }

    @Override
    public void removeUserRoleApiSkip(String uri, String method) {

    }

    @Override
    public UserRoleApiSkipDto getUserRoleApiSkip(String uri, String method) {
        return null;
    }

    @Override
    public boolean isApiSkipped(String uri, String method) {
        logger.debug("Checking if API is skipped for URI: {}, Method: {}", uri, method);
        return userRoleApiSkipRepository.existsByUriAndMethod(uri, method);
    }
}
