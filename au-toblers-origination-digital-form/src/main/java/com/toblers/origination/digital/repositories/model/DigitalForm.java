package com.toblers.origination.digital.repositories.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class DigitalForm {

    private String pk;
    private String sk;
    public String status;
    public String products;
    public String createdAt;
    public String statusCreatedAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(value = "PK")
    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute(value = "SK")
    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    @DynamoDbAttribute(value = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DynamoDbAttribute(value = "products")
    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    @DynamoDbAttribute(value = "createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @DynamoDbSecondarySortKey(indexNames = "statusAndCreatedAt")
    @DynamoDbAttribute(value = "statusCreatedAt")
    public String getStatusCreatedAt() {
        return statusCreatedAt;
    }

    public void setStatusCreatedAt(String statusCreatedAt) {
        this.statusCreatedAt = statusCreatedAt;
    }
}
