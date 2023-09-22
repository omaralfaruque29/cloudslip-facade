package com.cloudslip.facade.service;

import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.SaveCompanyDTO;
import com.cloudslip.facade.helper.registration.RegisterCompanyHelper;
import com.cloudslip.facade.model.Company;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.util.Utils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RegisterCompanyHelper registerCompanyHelper;

    /**
     * Create a compnay.
     *
     * @param dto the entity to register as a company
     * @return the persisted entity
     */
    public ResponseDTO<Company> createCompany(SaveCompanyDTO dto) {
        log.debug("Request to create company : {}", dto);
        User requester = Utils.getRequester();
        return (ResponseDTO<Company>) registerCompanyHelper.execute(dto, requester);
    }

    /**
     * Register a compnay.
     *
     * @param dto the entity to register as a company
     * @return the persisted entity
     */
    public ResponseDTO<Company> registerCompany(SaveCompanyDTO dto) {
        log.debug("Request to register company : {}", dto);
        return (ResponseDTO<Company>) registerCompanyHelper.execute(dto, null);
    }

}
