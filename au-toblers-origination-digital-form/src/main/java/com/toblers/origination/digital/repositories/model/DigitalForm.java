package com.toblers.origination.digital.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "digital_form")
public class DigitalForm {

    @Id
    private DigitalFormId digitalFormId;

    public String status;
    public String products;

    @DynamoDBAttribute
    public String getStatus() {
        return status;
    }

    @DynamoDBAttribute
    public String getProducts() {
        return products;
    }

    @DynamoDBHashKey(attributeName = "FormNumber")
    public String getPk() {
        return digitalFormId.getPk();
    }

    public void setPk(String pk) {
        digitalFormId.setPk(pk);
    }

    @DynamoDBRangeKey(attributeName = "info")
    public String getSk() {
        return digitalFormId.getSk();
    }

    public void setSk(String sk) {
        digitalFormId.setSk(sk);
    }


    public DigitalFormId getDigitalFormId() {
        return digitalFormId;
    }

    public void setDigitalFormId(DigitalFormId digitalFormId) {
        this.digitalFormId = digitalFormId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
