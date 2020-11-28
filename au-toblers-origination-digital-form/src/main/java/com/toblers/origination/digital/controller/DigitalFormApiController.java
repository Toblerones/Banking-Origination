package com.toblers.origination.digital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-11-28T15:39:41.468334+11:00[Australia/Sydney]")

@Controller
@RequestMapping("${openapi.bankingOrigination.base-path:/api}")
public class DigitalFormApiController implements DigitalFormApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public DigitalFormApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
