package com.academy.common.repository;


import com.academy.common.entity.ConfigVar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigVarRepository extends MongoRepository<ConfigVar, String> {
}
