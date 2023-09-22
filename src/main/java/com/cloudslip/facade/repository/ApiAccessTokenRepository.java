package com.cloudslip.facade.repository;


import com.cloudslip.facade.enums.Status;
import com.cloudslip.facade.model.ApiAccessToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ApiAccessTokenRepository extends MongoRepository<ApiAccessToken, ObjectId> {
    Optional<ApiAccessToken> findByUserId(final ObjectId objectId);

    Optional<ApiAccessToken> findByUserIdAndStatus(final ObjectId objectId, Status status);

    Optional<ApiAccessToken> findByAccessToken(final String accessToken);
}