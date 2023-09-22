package com.cloudslip.facade.service;

import com.cloudslip.facade.dto.GetObjectInputDTO;
import com.cloudslip.facade.dto.appissue.CreateAppIssueDTO;
import com.cloudslip.facade.dto.appissue.UpdateAppIssueDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.helper.app_issue.GetAllAllowedUserForTaggingHelper;
import com.cloudslip.facade.helper.app_issue.ValidateTaggedUserForCreateAppIssueHelper;
import com.cloudslip.facade.helper.app_issue.ValidateTaggedUserForUpdateAppIssueHelper;
import com.cloudslip.facade.model.User;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppIssueService {

    private final Logger log = LoggerFactory.getLogger(AppIssueService.class);

    @Autowired
    private ValidateTaggedUserForCreateAppIssueHelper validateTaggedUserForCreateAppIssueHelper;

    @Autowired
    private ValidateTaggedUserForUpdateAppIssueHelper validateTaggedUserForUpdateAppIssueHelper;

    @Autowired
    private GetAllAllowedUserForTaggingHelper getAllAllowedUserForTaggingHelper;

    /**
     * Validate Tagged User for an Application Issue For Create
     *
     * @param input the entity to save
     * @return the persisted entity
     */
    public ResponseDTO updateAppIssueCreateDtoWithTaggedUserList(CreateAppIssueDTO input, User requester) {
        log.debug("REST request to validate tagged user for an application issue : {}", input);
        return (ResponseDTO) validateTaggedUserForCreateAppIssueHelper.execute(input, requester);
    }

    /**
     * Validate Tagged User for an Application Issue For Update
     *
     * @param input the entity to save
     * @return the persisted entity
     */
    public ResponseDTO updateAppIssueUpdateDtoWithTaggedUserList(UpdateAppIssueDTO input, User requester) {
        log.debug("REST request to validate tagged user for an application issue : {}", input);
        return (ResponseDTO) validateTaggedUserForUpdateAppIssueHelper.execute(input, requester);
    }

    /**
     * Get All Allowed User list for tagging By Application
     *
     * @param applicationId to get
     * @return the persisted entity
     */
    public ResponseDTO getAllAllowedUserForTagging(ObjectId applicationId, User requester, ResponseDTO response) {
        log.debug("REST request to get all allowed user for tagging");
        return (ResponseDTO) getAllAllowedUserForTaggingHelper.execute(new GetObjectInputDTO(applicationId), requester, response);
    }
}
