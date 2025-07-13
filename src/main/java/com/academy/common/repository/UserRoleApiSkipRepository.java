package com.academy.common.repository;

import com.academy.common.entity.UserRoleApiSkip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface UserRoleApiSkipRepository extends MongoRepository<UserRoleApiSkip, String> {

    UserRoleApiSkip findByUriAndMethod(String uri, String method);

    boolean existsByUriAndMethod(String uri, String method);
}
