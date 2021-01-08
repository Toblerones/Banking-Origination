package com.toblers.origination.digital.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.toblers.origination.digital.model.Customer;
import com.toblers.origination.digital.model.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DigitalForm
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-01-08T21:47:42.983937+11:00[Australia/Sydney]")

public class DigitalForm   {
  @JsonProperty("formId")
  private String formId;

  @JsonProperty("customer")
  @Valid
  private List<Customer> customer = null;

  @JsonProperty("product")
  private Product product;

  @JsonProperty("status")
  private String status;

  public DigitalForm formId(String formId) {
    this.formId = formId;
    return this;
  }

  /**
   * digital form number
   * @return formId
  */
  @ApiModelProperty(value = "digital form number")


  public String getFormId() {
    return formId;
  }

  public void setFormId(String formId) {
    this.formId = formId;
  }

  public DigitalForm customer(List<Customer> customer) {
    this.customer = customer;
    return this;
  }

  public DigitalForm addCustomerItem(Customer customerItem) {
    if (this.customer == null) {
      this.customer = new ArrayList<>();
    }
    this.customer.add(customerItem);
    return this;
  }

  /**
   * Get customer
   * @return customer
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<Customer> getCustomer() {
    return customer;
  }

  public void setCustomer(List<Customer> customer) {
    this.customer = customer;
  }

  public DigitalForm product(Product product) {
    this.product = product;
    return this;
  }

  /**
   * Get product
   * @return product
  */
  @ApiModelProperty(value = "")

  @Valid

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public DigitalForm status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  @ApiModelProperty(value = "")


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DigitalForm digitalForm = (DigitalForm) o;
    return Objects.equals(this.formId, digitalForm.formId) &&
        Objects.equals(this.customer, digitalForm.customer) &&
        Objects.equals(this.product, digitalForm.product) &&
        Objects.equals(this.status, digitalForm.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(formId, customer, product, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DigitalForm {\n");
    
    sb.append("    formId: ").append(toIndentedString(formId)).append("\n");
    sb.append("    customer: ").append(toIndentedString(customer)).append("\n");
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

