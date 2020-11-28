package com.tobs.origination.digital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-11-28T14:57:28.327430+11:00[Australia/Sydney]")

@Controller
@RequestMapping("${openapi.bankingOrigination.base-path:/api}")
public class ApplicationApiController implements ApplicationApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ApplicationApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
