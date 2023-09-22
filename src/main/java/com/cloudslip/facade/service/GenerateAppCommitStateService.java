package com.cloudslip.facade.service;

import com.cloudslip.facade.dto.GetHeaderAppCommitStateDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.helper.app_commit_state.GetHeaderAppCommitStateHelper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateAppCommitStateService {

    private final Logger log = LoggerFactory.getLogger(GenerateAppCommitStateService.class);

    @Autowired
    GetHeaderAppCommitStateHelper getHeaderAppCommitStateHelper;

    /**
     * Check Git Response Validity.
     *
     * @param actionId the entity to save
     * @return the persisted entity
     */
    public ResponseDTO getHeader(String userAgent, String githubDelivery, String githubEvent, String apiAccessToken, String actionId, String applicationId) {
        log.debug("REST request to check if git response is valid : {}", actionId);
        return (ResponseDTO) getHeaderAppCommitStateHelper.execute(new GetHeaderAppCommitStateDTO(userAgent, githubDelivery, githubEvent, apiAccessToken, applicationId), null, new ObjectId(actionId));
    }
}
