package com.cloudslip.facade.repository;


import com.cloudslip.facade.enums.Status;
import com.cloudslip.facade.model.UserWebSocketSession;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserWebSocketSessionRepository extends MongoRepository<UserWebSocketSession, ObjectId> {
    List<UserWebSocketSession> findAllByUserIdAndStatus(final ObjectId objectId, final Status status);

    Optional<UserWebSocketSession> findByUserIdAndSimpSessionIdAndStatus(final ObjectId userId, final String simpSessionId, final Status status);

    Optional<UserWebSocketSession> findBySimpSessionIdAndWebSocketTopicAndStatus(final String simpSessionId, final String webSocketTopic, final Status status);
}