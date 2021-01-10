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
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

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
            final DigitalFormDao formDao = new DigitalFormDao();

            // Form Item
            formDao.setPk(String.format("%s#%s", "FORM", digitalForm.getFormId()));
            formDao.setSk(String.format("%s#%s", "INFO", digitalForm.getFormId()));
            formDao.setProducts(digitalForm.getProduct().getProductCodes().get(0));
            formDao.setStatus(digitalForm.getStatus());

            digitalformTable.updateItem(request -> request.item(formDao).ignoreNulls(true)).get();

            // Applicant Item
            for (Customer customer : digitalForm.getCustomer()) {
                final DigitalFormDao applicantDao = new DigitalFormDao();
                String primaryKey = String.format("%s#%s", "FORM", digitalForm.getFormId());
                String sortKey = String.format("%s#%s#%s", "APPLICANT", customer.getIndex(), digitalForm.getFormId());

                applicantDao.setPk(primaryKey);
                applicantDao.setSk(sortKey);
                applicantDao.setFirstName(customer.getFirstName());
                applicantDao.setLastName(customer.getLastName());
                applicantDao.setMobile(customer.getMobileNumber());
                applicantDao.setEmail(customer.getEmail());
                applicantDao.setDob(customer.getDateOfBirth());
                digitalformTable.updateItem(request -> request.item(applicantDao).ignoreNulls(true)).get();

                // Address Item
                for(Address address : customer.getAddresses()) {

                    final DigitalFormDao addressDao = new DigitalFormDao();
                    String addressId = UUID.randomUUID().toString();
                    addressDao.setPk(String.format("%s#%s#%s", "ADDRESS", address.getType(), addressId));
                    addressDao.setSk(sortKey);
                    addressDao.setAddressId(addressId);
                    addressDao.setApplicantId(String.format("%s#%s", customer.getIndex(), digitalForm.getFormId()));
                    addressDao.setAddressType(address.getType());
                    addressDao.setAddressDetail(address.getDetail());
                    addressDao.setAddressCountry(address.getCountry());
                    digitalformTable.updateItem(request -> request.item(addressDao).ignoreNulls(true)).get();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
