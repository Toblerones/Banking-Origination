package com.toblers.origination.digital.domain;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Product
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-12-30T15:18:13.367822+11:00[Australia/Sydney]")

public class Product   {
  @JsonProperty("productCodes")
  @Valid
  private List<String> productCodes = null;

  @JsonProperty("mainCurrency")
  private String mainCurrency;

  @JsonProperty("subCurrencies")
  @Valid
  private List<String> subCurrencies = null;

  public Product productCodes(List<String> productCodes) {
    this.productCodes = productCodes;
    return this;
  }

  public Product addProductCodesItem(String productCodesItem) {
    if (this.productCodes == null) {
      this.productCodes = new ArrayList<>();
    }
    this.productCodes.add(productCodesItem);
    return this;
  }

  /**
   * Get productCodes
   * @return productCodes
  */
  @ApiModelProperty(value = "")


  public List<String> getProductCodes() {
    return productCodes;
  }

  public void setProductCodes(List<String> productCodes) {
    this.productCodes = productCodes;
  }

  public Product mainCurrency(String mainCurrency) {
    this.mainCurrency = mainCurrency;
    return this;
  }

  /**
   * Get mainCurrency
   * @return mainCurrency
  */
  @ApiModelProperty(value = "")


  public String getMainCurrency() {
    return mainCurrency;
  }

  public void setMainCurrency(String mainCurrency) {
    this.mainCurrency = mainCurrency;
  }

  public Product subCurrencies(List<String> subCurrencies) {
    this.subCurrencies = subCurrencies;
    return this;
  }

  public Product addSubCurrenciesItem(String subCurrenciesItem) {
    if (this.subCurrencies == null) {
      this.subCurrencies = new ArrayList<>();
    }
    this.subCurrencies.add(subCurrenciesItem);
    return this;
  }

  /**
   * Get subCurrencies
   * @return subCurrencies
  */
  @ApiModelProperty(value = "")


  public List<String> getSubCurrencies() {
    return subCurrencies;
  }

  public void setSubCurrencies(List<String> subCurrencies) {
    this.subCurrencies = subCurrencies;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return Objects.equals(this.productCodes, product.productCodes) &&
        Objects.equals(this.mainCurrency, product.mainCurrency) &&
        Objects.equals(this.subCurrencies, product.subCurrencies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productCodes, mainCurrency, subCurrencies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Product {\n");
    
    sb.append("    productCodes: ").append(toIndentedString(productCodes)).append("\n");
    sb.append("    mainCurrency: ").append(toIndentedString(mainCurrency)).append("\n");
    sb.append("    subCurrencies: ").append(toIndentedString(subCurrencies)).append("\n");
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

