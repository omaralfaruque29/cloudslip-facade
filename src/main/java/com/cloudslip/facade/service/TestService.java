package com.cloudslip.facade.service;

import com.cloudslip.facade.enums.ActionStatus;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.repository.SystemActionRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing SystemAction.
 */
@Service
@Transactional(rollbackFor = ApiErrorException.class)
public class TestService {

    private final Logger log = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private SystemActionRepository systemActionRepository;


    public void createNew() {
        SystemAction systemAction = new SystemAction("This is rollback testing");
        systemAction.setId(ObjectId.get());
        systemAction = systemActionRepository.save(systemAction);
        throw new ApiErrorException(this.getClass().getName());
    }
}
