package com.toblers.origination.digital.repositories;

import com.toblers.origination.digital.model.DigitalForm;
import com.toblers.origination.digital.repositories.model.DigitalFormDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

@Service
public class DigitalFormRepository {

    DynamoDbAsyncTable<DigitalFormDao> digitalformTable;

    DigitalFormRepository(@Autowired
                                  DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient){
        this.digitalformTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));
    }


    public void createDigitalForm(DigitalForm digitalForm){
        DigitalFormDao formDao = new DigitalFormDao();

        formDao.setPk(String.format("%s#%s", "FORM", digitalForm.getFormId()));
        formDao.setSk(String.format("%s#%s", "INFO", digitalForm.getFormId()));
        formDao.setProducts(digitalForm.getProduct().getProductCodes().get(0));
        formDao.setStatus(digitalForm.getStatus());
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("mmHHddmmyyyy"));
        formDao.setCreatedAt(createdAt);
        formDao.setStatusCreatedAt(String.format("%s#%s", digitalForm.getStatus(), digitalForm.getFormId(), createdAt));
        try {
            digitalformTable.putItem(formDao).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void getDigitalFormById(){

    }

    public void updateDigitalForm(DigitalForm digitalForm){
        DigitalFormDao formDao = new DigitalFormDao();

        formDao.setPk(String.format("%s#%s", "FORM", digitalForm.getFormId()));
        formDao.setSk(String.format("%s#%s", "INFO", digitalForm.getFormId()));
        formDao.setProducts(digitalForm.getProduct().getProductCodes().get(0));
        formDao.setStatus(digitalForm.getStatus());
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("mmHHddmmyyyy"));
        formDao.setCreatedAt(createdAt);
        formDao.setStatusCreatedAt(String.format("%s#%s", digitalForm.getStatus(), digitalForm.getFormId(), createdAt));
        try {
            digitalformTable.putItem(formDao).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
