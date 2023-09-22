package com.cloudslip.facade.helper.user;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.UpdateUserDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserInfo;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UpdateUserHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(UpdateUserHelper.class);

    private UpdateUserDTO input;
    private ResponseDTO<User> output = new ResponseDTO<User>();

    private User existingUser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private ApplicationProperties applicationProperties;


    protected void init(BaseInput input, Object... extraParams) {
        this.input = (UpdateUserDTO) input;
        this.setOutput(output);
    }

    protected void checkPermission() {
        if (requester == null || (!requester.hasAuthority(Authority.ROLE_SUPER_ADMIN) && !requester.hasAuthority(Authority.ROLE_ADMIN))) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void checkValidity() {
        if(input.getUserId() == null) {
            output.generateErrorResponse("User id is missing in the params");
            throw new ApiErrorException(this.getClass().getName());
        }
        Optional<User> tempUser = userRepository.findById(input.getUserId());
        if(!tempUser.isPresent() || !tempUser.get().isValid()) {
            output.generateErrorResponse("User doesn't exists with the given id");
            throw new ApiErrorException(this.getClass().getName());
        }
        existingUser = tempUser.get();
    }

    protected void doPerform() {
        this.updateUser();
        this.updateUserInfoForUser();
    }

    private void updateUser() {
        existingUser.setEnabled(input.isEnabled());
        List<Authority> authorities = new ArrayList<>();

        for(int i = 0; i < input.getAuthorities().size(); i++) {
            if(input.getAuthorities().get(i) == Authority.ROLE_DEV) {
                authorities.add(Authority.ROLE_DEV);
            } else if(input.getAuthorities().get(i) == Authority.ROLE_OPS) {
                authorities.add(Authority.ROLE_OPS);
            } else if(input.getAuthorities().get(i) == Authority.ROLE_ADMIN) {
                authorities.add(Authority.ROLE_ADMIN);
            }
        }

        existingUser.setAuthorities(authorities);
        existingUser.setUpdateDate(String.valueOf(LocalDateTime.now()));
        existingUser.setUpdatedBy(requester.getUsername());
        existingUser.setLastUpdateActionId(actionId);
        if(requester.hasAuthority(Authority.ROLE_SUPER_ADMIN) && input.getUserType() != null) {
            existingUser.setUserType(input.getUserType());
        }
        userRepository.save(existingUser);
    }

    private void updateUserInfoForUser() {
        HttpHeaders headers = Utils.generateHttpHeaders(requester, actionId.toHexString());
        HttpEntity<UpdateUserDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO<UserInfo> response = new ResponseDTO<UserInfo>();
        boolean errorWhileUpdatingUserInfo = false;
        try {
            Object responseObject = restTemplate.putForObject( applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/" + input.getUserId(), request, ResponseDTO.class);
            response = (ResponseDTO) ((ResponseEntity<?>) responseObject).getBody();
            UserInfo userInfo = objectMapper.convertValue(response.getData(), UserInfo.class);
            if(userInfo == null) {
                errorWhileUpdatingUserInfo = true;
            }
            existingUser.setUserInfo(userInfo);
            output.generateSuccessResponse(userRepository.save(existingUser), "User updated");
        } catch (ResourceAccessException ex) {
            log.info(ex.getMessage());
            errorWhileUpdatingUserInfo = true;
        } catch (HttpClientErrorException ex) {
            log.info(ex.getMessage());
            errorWhileUpdatingUserInfo = true;
        }

        if(errorWhileUpdatingUserInfo) {
            output.generateErrorResponse(response.getMessage());
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }
}
