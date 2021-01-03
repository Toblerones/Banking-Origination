package com.toblers.origination.digital.services.impl;

import com.toblers.origination.digital.repositories.DigitalFormRepository;
import com.toblers.origination.digital.repositories.model.DigitalForm;
import com.toblers.origination.digital.services.DigitalFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Service("DEFAULT-DIGITAL_FORM-SERVICE")
public class DigitalFormSaveService implements DigitalFormService {

    @Autowired
    DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Override
    public boolean handleDigitalForm() {

        DynamoDbAsyncTable<DigitalForm> digitalformTable = dynamoDbEnhancedAsyncClient.table("digital_form", TableSchema.fromBean(DigitalForm.class));
        DigitalForm form = new DigitalForm();
        form.setPk("FORM#ABC123");
        form.setSk("INFO#ABC123");
        form.setProducts("EGA");
        form.setStatus("IC");
        digitalformTable.putItem(form);
        return false;
    }
}
