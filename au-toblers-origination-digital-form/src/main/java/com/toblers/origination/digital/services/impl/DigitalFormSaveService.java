package com.toblers.origination.digital.services.impl;

import com.toblers.origination.digital.services.DigitalFormService;
import org.springframework.stereotype.Service;

@Service("DEFAULT-DIGITAL_FORM-SERVICE")
public class DigitalFormSaveService implements DigitalFormService {

    @Override
    public boolean handleDigitalForm() {
        return false;
    }
}
