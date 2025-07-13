package com.academy.common.repository;

import com.academy.common.entity.AllowedIP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowedIPRepository extends MongoRepository<AllowedIP, String> {

    boolean existsByIpAddress(String ipAddress);

    AllowedIP findByIpAddress(String ipAddress);
}
