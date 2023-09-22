package com.cloudslip.facade.helper.user;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.DeleteObjectInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@Service
public class DeleteUserHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(DeleteUserHelper.class);

    private DeleteObjectInputDTO input;
    private ResponseDTO output = new ResponseDTO();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private ApplicationProperties applicationProperties;


    protected void init(BaseInput input, Object... extraParams) {
        this.input = (DeleteObjectInputDTO) input;
        this.setOutput(output);
    }

    protected void checkPermission() {
        if (requester == null || (!requester.hasAuthority(Authority.ROLE_SUPER_ADMIN) && !requester.hasAuthority(Authority.ROLE_ADMIN))) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void checkValidity() {
        if(input.getId() == null) {
            output.generateErrorResponse("Id is missing in params!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void doPerform() {
        Optional<User> user = userRepository.findById(input.getId());
        if(user.isPresent()) {
            user.get().setLastUpdateActionId(actionId);
            userRepository.delete(user.get());
            boolean errorWhileCreatingUserInfo = false;
            SystemAction systemAction = systemActionService.create(String.format("REST request to delete User Info: %s", input.getId()));
            HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
            HttpEntity<String> request = new HttpEntity<>("parameters", headers);
            try {
                ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/"+ input.getId(), HttpMethod.DELETE,request,ResponseDTO.class);
                if(!response.getBody().getStatus().name().equals("success")) {
                    errorWhileCreatingUserInfo = true;
                }
                output.generateSuccessResponse("UserInfo deleted");
            } catch (ResourceAccessException ex) {
                log.info(ex.getMessage());
                errorWhileCreatingUserInfo = true;
            } catch (HttpClientErrorException ex) {
                log.info(ex.getMessage());
                errorWhileCreatingUserInfo = true;
            }

            if(errorWhileCreatingUserInfo) {
                output.generateErrorResponse();
                throw new ApiErrorException(this.getClass().getName());
            }
            output.generateSuccessResponse(null, "User deleted");
        } else {
            output.generateErrorResponse("User not found to delete!");
        }
    }

    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }

}
