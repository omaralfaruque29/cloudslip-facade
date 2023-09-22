package com.cloudslip.facade.helper.app_issue;

import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.GetObjectInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.appissue.GetAllAllowedUserForTaggingResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserInfo;
import com.cloudslip.facade.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllAllowedUserForTaggingHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(GetAllAllowedUserForTaggingHelper.class);

    private GetObjectInputDTO input;
    private ResponseDTO output = new ResponseDTO();

    private ResponseDTO response = new ResponseDTO();
    private GetAllAllowedUserForTaggingResponseDTO getAllAllowedUserForTaggingResponseDTO;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    public void init(BaseInput input, Object... extraParams) {
        this.input = (GetObjectInputDTO) input;
        this.setOutput(output);
        this.response = (ResponseDTO) extraParams[0];
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.getAllAllowedUserForTaggingResponseDTO = objectMapper.convertValue(response.getData(), new TypeReference<GetAllAllowedUserForTaggingResponseDTO>() { });
    }


    protected void checkPermission() {
        if (requester == null || requester.hasAuthority(Authority.ANONYMOUS)) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void checkValidity() {
    }


    protected void doPerform() {
        List<ObjectId> userInfoIdList = this.getUserInfoIdList(getAllAllowedUserForTaggingResponseDTO.getUserInfoList());
        List<User> taggedUserList = userRepository.findAllByUserInfoIdInOrUserInfoCompanyObjectIdAndAuthoritiesAuthorityOrAuthoritiesAuthority(userInfoIdList, getAllAllowedUserForTaggingResponseDTO.getApplicationTeam().getCompanyObjectId(), Authority.ROLE_ADMIN, Authority.ROLE_SUPER_ADMIN);
        output.generateSuccessResponse(taggedUserList, "Allowed user list for tagging");
    }

    protected void postPerformCheck() {
    }

    protected void doRollback() {

    }

    /*
        Get User Info Id List From User Info List
     */
    private List<ObjectId> getUserInfoIdList(List<UserInfo> userInfoList) {
        List<ObjectId> userInfoIdList = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            if (!userInfo.getUserId().equals(requester.getObjectId().toString())) {
                userInfoIdList.add(userInfo.getObjectId());
            }
        }
        return  userInfoIdList;
    }
}
