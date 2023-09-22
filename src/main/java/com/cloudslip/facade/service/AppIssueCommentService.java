package com.cloudslip.facade.service;

import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.app_issue_comment.AddAppIssueCommentDTO;
import com.cloudslip.facade.dto.app_issue_comment.UpdateAppIssueCommentDTO;
import com.cloudslip.facade.helper.app_issue_comment.ValidateTaggedUserForAddAppIssueCommentHelper;
import com.cloudslip.facade.helper.app_issue_comment.ValidateTaggedUserForUpdateAppIssueCommentHelper;
import com.cloudslip.facade.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppIssueCommentService {

    private final Logger log = LoggerFactory.getLogger(AppIssueCommentService.class);

    @Autowired
    private ValidateTaggedUserForAddAppIssueCommentHelper validateTaggedUserForAddAppIssueCommentHelper;

    @Autowired
    private ValidateTaggedUserForUpdateAppIssueCommentHelper validateTaggedUserForUpdateAppIssueCommentHelper;

    /**
     * Validate Tagged User for an Add Comment to Application Issue
     *
     * @param input the entity to save
     * @return the persisted entity
     */
    public ResponseDTO updateAppIssueCommentAddDtoWithTaggedUserList(AddAppIssueCommentDTO input, User requester) {
        log.debug("REST request to validate tagged user for an application issue comment : {}", input);
        return (ResponseDTO) validateTaggedUserForAddAppIssueCommentHelper.execute(input, requester);
    }

    /**
     * Validate Tagged User for Update Comment to Application Issue
     *
     * @param input the entity to save
     * @return the persisted entity
     */
    public ResponseDTO updateAppIssueCommentUpdateDtoWithTaggedUserList(UpdateAppIssueCommentDTO input, User requester) {
        log.debug("REST request to validate tagged user for an application issue : {}", input);
        return (ResponseDTO) validateTaggedUserForUpdateAppIssueCommentHelper.execute(input, requester);
    }
}
