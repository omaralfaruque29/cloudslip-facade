package com.cloudslip.facade.service;

import com.cloudslip.facade.enums.ActionStatus;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.repository.SystemActionRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing SystemAction.
 */
@Service
@Transactional
public class SystemActionService {

    private final Logger log = LoggerFactory.getLogger(SystemActionService.class);

    private final SystemActionRepository systemActionRepository;


    public SystemActionService(SystemActionRepository systemActionRepository) {
        this.systemActionRepository = systemActionRepository;
    }


    /**
     * Save a systemAction.
     *
     * @param systemAction the entity to save
     * @return the persisted entity
     */
    public SystemAction save(SystemAction systemAction) {
        log.debug("Request to save SystemAction : {}", systemAction);
        return systemActionRepository.save(systemAction);
    }


    /**
     * Create a systemAction.
     *
     * @param details the details of systemAction
     * @return the persisted entity
     */
    public SystemAction create(@Nullable String details) {
        SystemAction systemAction = new SystemAction(details);
        systemAction.setId(ObjectId.get());
        return systemActionRepository.save(systemAction);
    }


    /**
     * Update a systemAction.
     *
     * @param systemAction the entity
     * @return the persisted entity
     */
    public SystemAction saveWithSuccess(SystemAction systemAction) {
        systemAction.setActionStatus(ActionStatus.SUCCESS);
        return this.save(systemAction);
    }


    /**
     * Update a systemAction.
     *
     * @param systemAction the entity
     * @param errorMessage the message containing error
     * @return the persisted entity
     */
    public SystemAction saveWithFailure(SystemAction systemAction, @Nullable String errorMessage) {
        systemAction.setActionStatus(ActionStatus.FAILED);
        systemAction.setErrorMessage(errorMessage);
        return this.save(systemAction);
    }


    /**
     * Get one systemAction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SystemAction findById(ObjectId id) {
        log.debug("Request to get SystemAction : {}", id);
        Optional<SystemAction> systemAction = systemActionRepository.findById(id);
        return systemAction.isPresent() ? systemAction.get() :  null;
    }
}
