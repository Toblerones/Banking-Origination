package com.toblers.origination.digital.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * Address
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-12-30T15:18:13.367822+11:00[Australia/Sydney]")

public class Address   {
  @JsonProperty("type")
  private String type;

  @JsonProperty("detail")
  private String detail;

  @JsonProperty("country")
  private String country;

  public Address type(String type) {
    this.type = type;
    return this;
  }

  /**
   * address type
   * @return type
  */
  @ApiModelProperty(value = "address type")


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Address detail(String detail) {
    this.detail = detail;
    return this;
  }

  /**
   * address detail
   * @return detail
  */
  @ApiModelProperty(value = "address detail")


  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public Address country(String country) {
    this.country = country;
    return this;
  }

  /**
   * country of address
   * @return country
  */
  @ApiModelProperty(value = "country of address")


  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Address address = (Address) o;
    return Objects.equals(this.type, address.type) &&
        Objects.equals(this.detail, address.detail) &&
        Objects.equals(this.country, address.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, detail, country);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Address {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
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

