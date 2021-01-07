package com.toblers.origination.digital.repositories.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;

@DynamoDbBean
public class DigitalFormDao {

    private String pk;
    private String sk;
    public String status;
    public String products;
    public String createdAt;
    public String productsCreatedAt;
    public String firstName;
    public String lastName;
    public String mobile;
    public String email;
    public String dob;
    public String formId;
    public String applicantId;
    public String addressId;
    public String addressType;
    public String addressDetail;
    public String addressCountry;
    public Finance financeInfo;

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "productsAndCreatedAt")
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

    @DynamoDbSecondarySortKey(indexNames = "productsAndCreatedAt")
    @DynamoDbAttribute(value = "productsCreatedAt")
    public String getProductsCreatedAt() {
        return productsCreatedAt;
    }

    public void setProductsCreatedAt(String productsCreatedAt) {
        this.productsCreatedAt = productsCreatedAt;
    }

    @DynamoDbAttribute(value = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @DynamoDbAttribute(value = "last_name")
    public String getLastName() {
        return lastName;
    }

    @DynamoDbAttribute(value = "mobile")
    public String getMobile() {
        return mobile;
    }

    @DynamoDbAttribute(value = "email")
    public String getEmail() {
        return email;
    }

    @DynamoDbAttribute(value = "date_of_birth")
    public String getDob() {
        return dob;
    }

    @DynamoDbAttribute(value = "form_id")
    public String getFormId() {
        return formId;
    }

    @DynamoDbAttribute(value = "applicant_id")
    public String getApplicantId() {
        return applicantId;
    }

    @DynamoDbAttribute(value = "address_id")
    public String getAddressId() {
        return addressId;
    }

    @DynamoDbAttribute(value = "address_type")
    public String getAddressType() {
        return addressType;
    }

    @DynamoDbAttribute(value = "address_detail")
    public String getAddressDetail() {
        return addressDetail;
    }

    @DynamoDbAttribute(value = "address_country")
    public String getAddressCountry() {
        return addressCountry;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    @DynamoDbAttribute(value = "finance_info")
    public Finance getFinanceInfo() {
        return financeInfo;
    }

    public void setFinanceInfo(Finance financeInfo) {
        this.financeInfo = financeInfo;
    }

    @DynamoDbBean
    public static class Finance {
        public BigDecimal income;
        public BigDecimal credit;
        public BigDecimal mortgage;

        @DynamoDbAttribute(value = "income")
        public BigDecimal getIncome() {
            return income;
        }

        public void setIncome(BigDecimal income) {
            this.income = income;
        }

        @DynamoDbAttribute(value = "credit")
        public BigDecimal getCredit() {
            return credit;
        }

        public void setCredit(BigDecimal credit) {
            this.credit = credit;
        }

        @DynamoDbAttribute(value = "mortgage")
        public BigDecimal getMortgage() {
            return mortgage;
        }

        public void setMortgage(BigDecimal mortgage) {
            this.mortgage = mortgage;
        }
    }

}
