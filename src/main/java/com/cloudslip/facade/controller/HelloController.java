package com.cloudslip.facade.controller;

import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.TestInputDTO;
import com.cloudslip.facade.dto.TestOutputDTO;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.model.Company;
import com.cloudslip.facade.helper.HelloHelper;
import com.cloudslip.facade.service.TestService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class HelloController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HelloHelper helloService;

    @Autowired
    private TestService testService;

    @RequestMapping("/")
    public String index() {
        TestInputDTO input = new TestInputDTO();
        input.setMessage("This is a test message");
        ResponseDTO<TestOutputDTO> output = (ResponseDTO<TestOutputDTO>) helloService.execute(input, null);
        return "This is CloudSlip Service Facade";
    }

    @RequestMapping(value = "/ms-test", method = RequestMethod.GET)
    public ResponseEntity<?> getCompanyList() throws URISyntaxException {
        //Fetching as LinkedHashMap List
        List<?> companyList = restTemplate.getForObject("http://localhost:8081/api/company", List.class);

        //Mapping it to Company Object List
        List<Company> companies = objectMapper.convertValue(companyList, new TypeReference<List<Company>>() { });

        for (Object obj : companyList) {
            //Mapping LinkedHashMap objects to Company object
            Company company = objectMapper.convertValue(obj, Company.class);
            System.out.println(company);
        }
        return new ResponseEntity<>(companyList, HttpStatus.OK);
    }

    @RequestMapping(value = "/access-token-test", method = RequestMethod.GET)
    public ResponseEntity<?> testAccessToken() throws URISyntaxException {
        return new ResponseEntity<>(new ResponseDTO<>().generateSuccessResponse(null, "It works!!"), HttpStatus.OK);
    }

    @RequestMapping(value = "/test/rollback", method = RequestMethod.GET)
    public ResponseEntity<?> testRollback() {
        try {
            testService.createNew();
        } catch (ApiErrorException ex) {
            return new ResponseEntity<>(new ResponseDTO<>().generateSuccessResponse(null, "Tried"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDTO<>().generateSuccessResponse(null, "Tried"), HttpStatus.OK);
    }


    @RequestMapping(value="/log", method=RequestMethod.GET)
    public ResponseEntity<Object> downloadFile() throws IOException  {
        FileWriter filewriter = null;
        try {
            String log = "log: Cannot find template location";
            String filename = "C:\\Users\\CLOUDSLIP\\Desktop\\logFile.txt";

            filewriter = new FileWriter(filename);
            filewriter.write(log);
            filewriter.flush();

            File file = new File(filename);

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(log);
            return responseEntity;
        } catch (Exception e ) {
            return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if(filewriter!=null)
                filewriter.close();
        }
    }

}