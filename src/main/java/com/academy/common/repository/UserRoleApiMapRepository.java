package com.academy.common.repository;

import com.academy.common.entity.UserRoleApiMap;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleApiMapRepository extends MongoRepository<UserRoleApiMap, String> {

    List<UserRoleApiMap> findByPAthAndMethod(String path, String method);
}
