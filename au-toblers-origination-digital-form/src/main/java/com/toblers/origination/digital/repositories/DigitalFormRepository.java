package com.toblers.origination.digital.repositories;

import com.toblers.origination.digital.model.Address;
import com.toblers.origination.digital.model.Customer;
import com.toblers.origination.digital.model.DigitalForm;
import com.toblers.origination.digital.repositories.model.DigitalFormDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.sortBeginsWith;

@Service
public class DigitalFormRepository {

    DynamoDbAsyncTable<DigitalFormDao> asyncDigitalFormTable;
    DynamoDbTable<DigitalFormDao> digitalFormTable;
    DynamoDbIndex<DigitalFormDao> digitalFormInvertedIndex;

    DigitalFormRepository(@Autowired DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient,
                          @Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient){
        this.asyncDigitalFormTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));
        this.digitalFormTable = dynamoDbEnhancedClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));
        this.digitalFormInvertedIndex = digitalFormTable
                .index("invertedIndex");
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
            asyncDigitalFormTable.putItem(formDao).get();
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

            asyncDigitalFormTable.updateItem(request -> request.item(formDao).ignoreNulls(true)).get();

            // Applicant Item
            for (Customer customer : digitalForm.getCustomer()) {
                final DigitalFormDao applicantDao = new DigitalFormDao();
                String primaryKey = String.format("%s#%s", "FORM", digitalForm.getFormId());
                String applicantSortKey = String.format("%s#%s#%s", "APPLICANT", customer.getIndex(), digitalForm.getFormId());

                applicantDao.setPk(primaryKey);
                applicantDao.setSk(applicantSortKey);
                applicantDao.setFirstName(customer.getFirstName());
                applicantDao.setLastName(customer.getLastName());
                applicantDao.setMobile(customer.getMobileNumber());
                applicantDao.setEmail(customer.getEmail());
                applicantDao.setDob(customer.getDateOfBirth());
                asyncDigitalFormTable.updateItem(request -> request.item(applicantDao).ignoreNulls(true)).get();

                // Address Item
                for(Address address : customer.getAddresses()) {

                    String addressPkPrefix = String.format("%s#%s", "ADDRESS", address.getType());

                    // should have 1 address type record.
                    SdkIterable<Page<DigitalFormDao>> digitalForms = digitalFormInvertedIndex.query(
                            r -> r.queryConditional(
                                    sortBeginsWith(k -> k.partitionValue(applicantSortKey).sortValue(addressPkPrefix))));

                    List<DigitalFormDao> addressDaoList = digitalForms.stream().findFirst().get().items();

                    if(addressDaoList.isEmpty()) {
                        final DigitalFormDao addressDao = new DigitalFormDao();
                        String addressId = UUID.randomUUID().toString();
                        addressDao.setPk(String.format("%s#%s#%s", "ADDRESS", address.getType(), addressId));
                        addressDao.setSk(applicantSortKey);
                        addressDao.setAddressId(addressId);
                        addressDao.setApplicantId(String.format("%s#%s", customer.getIndex(), digitalForm.getFormId()));
                        addressDao.setAddressType(address.getType());
                        addressDao.setAddressDetail(address.getDetail());
                        addressDao.setAddressCountry(address.getCountry());
                        asyncDigitalFormTable.updateItem(request -> request.item(addressDao).ignoreNulls(true)).get();
                    } else {
                        DigitalFormDao lastAddressDao = addressDaoList.get(0);

                        lastAddressDao.setAddressType(address.getType());
                        lastAddressDao.setAddressDetail(address.getDetail());
                        lastAddressDao.setAddressCountry(address.getCountry());
                        asyncDigitalFormTable.updateItem(request -> request.item(lastAddressDao).ignoreNulls(true)).get();
                    }

                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
