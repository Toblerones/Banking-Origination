package com.toblers.origination.digital.repositories;

import com.toblers.origination.digital.model.Address;
import com.toblers.origination.digital.model.Customer;
import com.toblers.origination.digital.model.DigitalForm;
import com.toblers.origination.digital.repositories.model.DigitalFormDao;
import com.toblers.origination.digital.util.SecureRandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
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
        formDao.setProductsCreatedAt(String.format("%s#%s", digitalForm.getStatus(), digitalForm.getFormId(), createdAt));
        try {
            digitalformTable.putItem(formDao).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void getDigitalFormById(DigitalForm digitalForm){


    }

    public void updateDigitalForm(DigitalForm digitalForm){
        try {
            DigitalFormDao formDao = new DigitalFormDao();

            // Form Item
            formDao.setPk(String.format("%s#%s", "FORM", digitalForm.getFormId()));
            formDao.setSk(String.format("%s#%s", "INFO", digitalForm.getFormId()));
            formDao.setProducts(digitalForm.getProduct().getProductCodes().get(0));
            formDao.setStatus(digitalForm.getStatus());
            digitalformTable.updateItem(formDao).get();

            // Applicant Item
            for (Customer customer : digitalForm.getCustomer()) {
                formDao = new DigitalFormDao();
                String primaryKey = String.format("%s#%s", "FORM", digitalForm.getFormId());
                String sortKey = String.format("%s#%s#%s", "APPLICANT", customer.getIndex(), digitalForm.getFormId());

                formDao.setPk(primaryKey);
                formDao.setSk(sortKey);
                formDao.setFirstName(customer.getFirstName());
                formDao.setLastName(customer.getLastName());
                formDao.setMobile(customer.getMobileNumber());
                formDao.setEmail(customer.getEmail());
                formDao.setDob(customer.getDateOfBirth());
                digitalformTable.updateItem(formDao).get();

                // Address Item
                for(Address address : customer.getAddresses()) {
                    formDao = new DigitalFormDao();
                    String addressId = UUID.randomUUID().toString();
                    formDao.setPk(String.format("%s#%s", "ADDRESS", addressId));
                    formDao.setSk(sortKey);
                    formDao.setAddressId(addressId);
                    formDao.setApplicantId(String.format("%s#%s", customer.getIndex(), digitalForm.getFormId()));
                    formDao.setAddressType(address.getType());
                    formDao.setAddressDetail(address.getDetail());
                    formDao.setAddressCountry(address.getCountry());
                    digitalformTable.updateItem(formDao).get();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
