package com.cloudslip.facade.service;

import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserInfo;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.helper.user.*;
import com.cloudslip.facade.util.Utils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing User.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private AddUserHelper addUserHelper;

    @Autowired
    private UpdateUserHelper updateUserHelper;

    @Autowired
    private GetUserListHelper getUserListHelper;

    @Autowired
    private DeleteUserHelper deleteUserHelper;

    @Autowired
    private GetUserHelper getUserHelper;

    @Autowired
    private ChangeUserPasswordHelper changeUserPasswordHelper;

    @Autowired
    private GenerateUserListFromUserInfoListHelper generateUserListFromUserInfoListHelper;

    @Autowired
    private GenerateUserFromUserInfoHelper generateUserFromUserInfoHelper;

    @Autowired
    private UpdateUserInfoOfUserHelper updateUserInfoOfUserHelper;

    @Autowired
    private UpdateInitialSettingStatusHelper updateInitialSettingStatusHelper;



    /**
     * Save a user.
     *
     * @param input the entity to save
     * @return the persisted entity
     */
    public ResponseDTO<User> save(SaveUserDTO input, ObjectId actionId) {
        log.debug("Request to save User : {}", input);
        User requester = Utils.getRequester();
        return (ResponseDTO<User>) addUserHelper.execute(input, requester, actionId);
    }


    /**
     * Update a user.
     *
     * @param input the entity to save
     * @return the persisted entity
     */
    public ResponseDTO<User> update(UpdateUserDTO input, ObjectId actionId) {
        log.debug("Request to update User : {}", input);
        User requester = Utils.getRequester();
        return (ResponseDTO<User>) updateUserHelper.execute(input, requester, actionId);
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
        return (ResponseDTO) getUserListHelper.execute(input, requester, pageable);
    }


    /**
     * Get one user by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public ResponseDTO<User> findById(ObjectId id) {
        log.debug("Request to get User : {}", id);
        User requester = Utils.getRequester();
        return (ResponseDTO<User>) getUserHelper.execute(new GetObjectInputDTO(id), requester);
    }


    /**
     * Delete the user by id.
     *
     * @param id the id of the entity
     */
    public ResponseDTO delete(ObjectId id, ObjectId actionId) {
        log.debug("Request to delete User : {}", id);
        User requester = Utils.getRequester();
        return (ResponseDTO) deleteUserHelper.execute(new DeleteObjectInputDTO(id), requester, actionId);
    }


    /**
     * Change user password.
     *
     * @param input the current and new password
     * @return ResponseDTO with updated User data
     */
    public ResponseDTO<User> changePassword(ChangePasswordDTO input){
        log.debug("Request to change user's password");
        User requester = Utils.getRequester();
        return (ResponseDTO<User>) changeUserPasswordHelper.execute(input, requester);

    }


    /**
     * Generate User List from UserInfo List
     *
     * @param input GenerateUserListFromUserInfoListInputDTO
     * @return ResponseDTO with UserList data
     */
    public ResponseDTO generateUserListFromUserInfoList(GenerateUserListFromUserInfoListInputDTO input) {
        User requester = Utils.getRequester();
        return (ResponseDTO) generateUserListFromUserInfoListHelper.execute(input, requester);
    }


    /**
     * Generate User from UserInfo
     *
     * @param input GenerateUserFromUserInfoInputDTO
     * @return ResponseDTO with User data
     */
    public ResponseDTO generateUserFromUserInfo(GenerateUserFromUserInfoInputDTO input) {
        User requester = Utils.getRequester();
        return (ResponseDTO) generateUserFromUserInfoHelper.execute(input, requester);
    }


    public void updateUserInfoOfUser(List<UserInfo> userInfoList) {
        User requester = Utils.getRequester();
        updateUserInfoOfUserHelper.execute(new ListInputDTO<UserInfo>(userInfoList), requester);
    }

    public ResponseDTO updateInitialSettingStatus(UpdateInitialSettingStatusInputDTO input) {
        log.debug("Request to get all Users");
        User requester = Utils.getRequester();
        return (ResponseDTO) updateInitialSettingStatusHelper.execute(input, requester);
    }
}
