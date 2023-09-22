package com.cloudslip.facade.helper.user;

import com.cloudslip.facade.constant.ListFetchMode;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.GetListFilterInput;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.helper.AbstractHelper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GetUserListHelper extends AbstractHelper {
    private final Logger log = LoggerFactory.getLogger(GetUserListHelper.class);

    private GetListFilterInput input;
    private ResponseDTO output = new ResponseDTO();
    private Pageable pageable;


    @Autowired
    private UserRepository userRepository;


    protected void init(BaseInput input, Object... extraParams) {
        this.input = (GetListFilterInput) input;
        this.setOutput(output);
        pageable = (Pageable) extraParams[0];
    }

    protected void checkPermission() {
        if (requester == null || requester.hasAuthority(Authority.ANONYMOUS) || requester.hasAuthority(Authority.ROLE_AGENT_SERVICE)) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void checkValidity() {

    }


    protected void doPerform() {
        if(requester.hasAuthority(Authority.ROLE_SUPER_ADMIN)) {
            this.fetchForSuperAdmin();
        } else {
            this.fetchForCompanyAdmin();
        }
    }

    private void fetchForSuperAdmin() {
        if(input.getFilterParamsMap().containsKey("companyId")) {
            ObjectId companyId = new ObjectId(input.getFilterParamsMap().get("companyId"));
            if (input.getFilterParamsMap().containsKey("teamId")) {
                ObjectId teamId = new ObjectId (input.getFilterParamsMap().get("teamId"));
                this.fetchAllUserWithCompanyAndTeam(companyId, teamId);
            } else {
                this.fetchAllUserWithCompany(companyId);
            }
        } else {
            if (input.getFilterParamsMap().containsKey("teamId")) {
                ObjectId teamId = new ObjectId (input.getFilterParamsMap().get("teamId"));
                this.fetchAllUserWithTeam(teamId);
            } else {
                this.fetchAllUser();
            }
        }
    }

    private void fetchForCompanyAdmin() {
        if (input.getFilterParamsMap().containsKey("teamId")) {
            ObjectId teamId = new ObjectId (input.getFilterParamsMap().get("teamId"));
            this.fetchAllUserWithCompanyAndTeam(requester.getCompanyId(), teamId);
        } else {
            this.fetchAllUserWithCompany(requester.getCompanyId());
        }
    }

    private void fetchAllUserWithCompanyAndTeam(ObjectId companyId, ObjectId teamId) {
        if(input.getFetchMode() == null || input.getFetchMode().equals(ListFetchMode.PAGINATION)) {
            filterAndSetOutput(userRepository.findAllByUserInfoCompanyIdAndUserInfoTeamsId(pageable, companyId, teamId));
        } else if(input.getFetchMode() != null || input.getFetchMode().equals(ListFetchMode.ALL)) {
            filterAndSetOutput(userRepository.findAllByUserInfoCompanyIdAndUserInfoTeamsId(companyId ,teamId));
        } else {
            output.generateErrorResponse("Invalid params in fetch mode");
        }
    }

    private void fetchAllUserWithTeam(ObjectId teamId) {
        if(input.getFetchMode() == null || input.getFetchMode().equals(ListFetchMode.PAGINATION)) {
            filterAndSetOutput(userRepository.findAllByUserInfoTeamsId(pageable, teamId));
        } else if(input.getFetchMode() != null || input.getFetchMode().equals(ListFetchMode.ALL)) {
            filterAndSetOutput(userRepository.findAllByUserInfoTeamsId(teamId));
        } else {
            output.generateErrorResponse("Invalid params in fetch mode");
        }
    }

    private void fetchAllUserWithCompany(ObjectId companyId) {
        if(input.getFetchMode() == null || input.getFetchMode().equals(ListFetchMode.PAGINATION)) {
            filterAndSetOutput(userRepository.findAllByUserInfoCompanyId(pageable, companyId));
        } else if(input.getFetchMode() != null || input.getFetchMode().equals(ListFetchMode.ALL)) {
            filterAndSetOutput(userRepository.findAllByUserInfoCompanyId(companyId));
        } else {
            output.generateErrorResponse("Invalid params in fetch mode");
        }
    }

    private void fetchAllUser() {
        if (input.getFetchMode() == null || input.getFetchMode().equals(ListFetchMode.PAGINATION)) {
            filterAndSetOutput(userRepository.findAll(pageable));
        } else if (input.getFetchMode() != null || input.getFetchMode().equals(ListFetchMode.ALL)) {
            filterAndSetOutput(userRepository.findAll());
        } else {
            output.generateErrorResponse("Invalid params in fetch mode");
        }
    }

    private void filterAndSetOutput(Page<User> userPage) {
        output.generateSuccessResponse(userPage);
    }

    private void filterAndSetOutput(List<User> userList) {
        output.generateSuccessResponse(userList);
    }

    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }
}
