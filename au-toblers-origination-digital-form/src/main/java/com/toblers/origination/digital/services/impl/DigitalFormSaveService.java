package com.toblers.origination.digital.services.impl;

import com.toblers.origination.digital.repositories.DigitalFormRepository;
import com.toblers.origination.digital.repositories.model.DigitalFormId;
import com.toblers.origination.digital.services.DigitalFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("DEFAULT-DIGITAL_FORM-SERVICE")
public class DigitalFormSaveService implements DigitalFormService {

    @Autowired
    DigitalFormRepository repository;

    @Override
    public boolean handleDigitalForm() {

        DigitalFormId id = new DigitalFormId("ag","ba");
        repository.findById(id);
        return false;
    }
}
