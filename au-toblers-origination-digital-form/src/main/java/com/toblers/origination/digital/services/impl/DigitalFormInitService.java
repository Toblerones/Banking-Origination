package com.toblers.origination.digital.services.impl;

import com.toblers.origination.digital.model.DigitalForm;
import com.toblers.origination.digital.repositories.DigitalFormRepository;
import com.toblers.origination.digital.services.DigitalFormService;
import com.toblers.origination.digital.services.ServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service("DEFAULT-DIGITAL_FORM-SERVICE")
public class DigitalFormInitService implements DigitalFormService {

    @Autowired
    DigitalFormRepository digitalFormRepository;

    @Override
    public boolean handleDigitalForm(DigitalForm digitalForm) {

        if(StringUtils.isEmpty(digitalForm.getFormId())){
            String formId = digitalForm.getCustomer().get(0).getFirstName().substring(0,1) +
                    digitalForm.getCustomer().get(0).getLastName().substring(0,1) +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("mmHHddmmyyyy"));
            digitalForm.setFormId(formId);

            digitalForm.setStatus(ServiceConstants.FORM_STATUS_INITIAL);
            digitalFormRepository.createDigitalForm(digitalForm);
        } else {
            digitalForm.setStatus(ServiceConstants.FORM_STATUS_IN_PROGRESS);
            digitalFormRepository.updateDigitalForm(digitalForm);
        }


        return true;
    }
}
