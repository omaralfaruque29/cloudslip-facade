package com.cloudslip.facade.repository;


import com.cloudslip.facade.model.SystemAction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemActionRepository extends MongoRepository<SystemAction, ObjectId> {

}