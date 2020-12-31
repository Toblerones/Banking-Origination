package com.toblers.origination.digital.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

public class DigitalFormId {

    private String pk;
    private String sk;

    public DigitalFormId() {
    }
    public DigitalFormId(String pk, String sk) {
        this.pk = pk;
        this.sk = sk;
    }

    @DynamoDBHashKey(attributeName = "FormNumber")
    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @DynamoDBRangeKey(attributeName = "info")
    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }
}
