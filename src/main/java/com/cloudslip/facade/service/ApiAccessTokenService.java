package com.cloudslip.facade.service;

import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.helper.api_access_token.*;
import com.cloudslip.facade.model.ApiAccessToken;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.util.Utils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * Service Implementation for managing User.
 */
@Service
@Transactional
public class ApiAccessTokenService {

    private final Logger log = LoggerFactory.getLogger(ApiAccessTokenService.class);

    @Autowired
    private CreateApiAccessTokenHelper createApiAccessTokenHelper;

    @Autowired
    private UpdateApiAccessTokenHelper updateApiAccessTokenHelper;

    @Autowired
    private GetApiAccessTokenHelper getApiAccessTokenHelper;

    @Autowired
    private GetApiAccessTokenListHelper getApiAccessTokenListHelper;

    @Autowired
    private DeleteApiAccessTokenHelper deleteApiAccessTokenHelper;

    @Autowired
    private GetGitAccessTokenForCreateAppHelper getGitAccessTokenForCreateAppHelper;


    /**
     * Save a user.
     *
     * @param input the entity to save
     * @return the persisted entity
     */
    public ResponseDTO<ApiAccessToken> create(CreateApiAccessTokenDTO input, ObjectId actionId) {
        log.debug("Request to save User : {}", input);
        User requester = Utils.getRequester();
        return (ResponseDTO<ApiAccessToken>) createApiAccessTokenHelper.execute(input, requester, actionId);
    }


    /**
     * Update a user.
     *
     * @param input the entity to save
     * @return the persisted entity
     */
    public ResponseDTO<ApiAccessToken> update(UpdateUserDTO input, ObjectId actionId) {
        log.debug("Request to update User : {}", input);
        User requester = Utils.getRequester();
        return (ResponseDTO<ApiAccessToken>) updateApiAccessTokenHelper.execute(input, requester, actionId);
    }


    /**
     * Get all the users.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public ResponseDTO findAll(GetListFilterInput input, Pageable pageable) {
        log.debug("Request to get all Users");
        User requester = Utils.getRequester();
        return (ResponseDTO) getApiAccessTokenListHelper.execute(input, requester, pageable);
    }


    /**
     * Get one user by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public ResponseDTO<ApiAccessToken> findById(ObjectId id) {
        log.debug("Request to get User : {}", id);
        User requester = Utils.getRequester();
        return (ResponseDTO<ApiAccessToken>) getApiAccessTokenHelper.execute(new GetObjectInputDTO(id), requester);
    }


    /**
     * Delete the user by id.
     *
     * @param id the id of the entity
     */
    public ResponseDTO delete(ObjectId id, ObjectId actionId) {
        log.debug("Request to delete User : {}", id);
        User requester = Utils.getRequester();
        return (ResponseDTO) deleteApiAccessTokenHelper.execute(new DeleteObjectInputDTO(id), requester, actionId);
    }

    /**
     * Get token for creating app by user id.
     *
     * @param input the id of the entity
     */
    public ResponseDTO getGitAgentAccessTokenFromCompany(CreateApplicationTemplateDTO input) {
        log.debug("Request to get git agent token from company : {}", input);
        User requester = Utils.getRequester();
        return (ResponseDTO) getGitAccessTokenForCreateAppHelper.execute(input, requester);
    }
}
