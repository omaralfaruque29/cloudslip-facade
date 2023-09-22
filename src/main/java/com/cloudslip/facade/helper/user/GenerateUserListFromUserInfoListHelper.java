package com.cloudslip.facade.helper.user;

import com.cloudslip.facade.constant.ListFetchMode;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.GenerateUserListFromUserInfoListInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.exception.model.ServiceException;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserInfo;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.helper.AbstractHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class GenerateUserListFromUserInfoListHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(GenerateUserListFromUserInfoListHelper.class);

    private GenerateUserListFromUserInfoListInputDTO input;
    private ResponseDTO output = new ResponseDTO();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    protected void init(BaseInput input, Object... extraParams) {
        this.input = (GenerateUserListFromUserInfoListInputDTO) input;
        this.setOutput(output);
    }

    protected void checkPermission() {
        if (requester == null || (!requester.hasAuthority(Authority.ROLE_SUPER_ADMIN) && !requester.hasAuthority(Authority.ROLE_ADMIN))) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void checkValidity() {

    }


    protected void doPerform() {
        if(input.getListFilterInput().getFetchMode() != null && input.getListFilterInput().getFetchMode().equals(ListFetchMode.ALL)) {
            this.generateForAllFetchMode();
        } else {
            this.generateForPaginationFetchMode();
        }
    }

    private void generateForAllFetchMode() {
        List<UserInfo> userInfoList = objectMapper.convertValue(input.getUserInfoListResponse().getData(), new TypeReference<List<UserInfo>>() { });
        List<User> userList = generateUserInfoListToUserList(userInfoList);
        output.generateSuccessResponse(userList);
    }

    private void generateForPaginationFetchMode() {
        LinkedHashMap<String, Object> userInfoPage = (LinkedHashMap<String, Object>) input.getUserInfoListResponse().getData();
        List<UserInfo> userInfoList = objectMapper.convertValue(userInfoPage.get("content"), new TypeReference<List<UserInfo>>() { });
        List<User> userList = generateUserInfoListToUserList(userInfoList);
        int totalElements = objectMapper.convertValue(userInfoPage.get("totalElements"), int.class);
        PageImpl<User> userPage = new PageImpl<User>(userList, input.getPageable(), totalElements);
        output.generateSuccessResponse(userPage);
    }

    private List<User> generateUserInfoListToUserList(List<UserInfo> userInfoList) {
        List<User> userList = new ArrayList<>();
        for(int i = 0; i < userInfoList.size(); i++) {
            Optional<User> user = userRepository.findById(new ObjectId(userInfoList.get(i).getUserId()));
            if(user.isPresent()) {
                user.get().setUserInfo(userInfoList.get(i));
                userList.add(user.get());
            }
        }
        return userList;
    }

    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }

}
