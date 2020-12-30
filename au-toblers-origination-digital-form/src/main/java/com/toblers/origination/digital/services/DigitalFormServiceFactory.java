package com.toblers.origination.digital.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class DigitalFormServiceFactory {

    @Autowired
    public Map<String, DigitalFormService> digitalFormServices;

    private static String DEFAULT_SERVICE_PREFIX = "DEFAULT";
    private static String SERVICE_BEAN_NAME = "DIGITAL_FORM-SERVICE";

    public DigitalFormService getService(String status){
        String serviceBeanName = "";

        if(StringUtils.isEmpty(status)){
            serviceBeanName = String.format("%s-%s", DEFAULT_SERVICE_PREFIX,SERVICE_BEAN_NAME);
        } else{
            serviceBeanName = String.format("%s-%s", status,SERVICE_BEAN_NAME);

        }
        return digitalFormServices.get(serviceBeanName);

    }
}
