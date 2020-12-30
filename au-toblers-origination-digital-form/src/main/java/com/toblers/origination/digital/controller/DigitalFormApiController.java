package com.toblers.origination.digital.controller;

import com.toblers.origination.digital.domain.DigitalForm;
import com.toblers.origination.digital.services.DigitalFormServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-12-30T15:18:13.367822+11:00[Australia/Sydney]")

@Controller
@RequestMapping("${openapi.bankingOrigination.base-path:/api}")
public class DigitalFormApiController implements DigitalFormApi {

    private final NativeWebRequest request;
    private final DigitalFormServiceFactory factory;
    @org.springframework.beans.factory.annotation.Autowired
    public DigitalFormApiController(NativeWebRequest request, @Autowired DigitalFormServiceFactory factory) {
        this.request = request;
        this.factory = factory;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> digitalFormPost(String xRequestID, String xSessionID, String xChannelID, @Valid DigitalForm digitalForm) {

        factory.getService(digitalForm.getStatus()).handleDigitalForm();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
