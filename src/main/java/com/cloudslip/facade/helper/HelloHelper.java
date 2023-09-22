package com.cloudslip.facade.helper;

import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.User;
import org.springframework.stereotype.Service;

@Service
public class HelloHelper extends AbstractHelper {

    private TestInputDTO input;
    private ResponseDTO<TestOutputDTO> output = new ResponseDTO();


    protected void init(BaseInput input, Object... extraParams) {
        this.input = (TestInputDTO) input;
        this.setOutput(output);
    }

    protected void checkPermission() {

    }

    protected void doPerform() {
        System.out.println("Hello is performing");
        System.out.println(input.getMessage());
        output.setStatus(ResponseStatus.success);
        output.setData(new TestOutputDTO("This is test output"));
    }

    protected void postPerformCheck() {

    }
}
