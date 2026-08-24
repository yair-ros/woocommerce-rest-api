
package com.woocommerce.beans.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.woocommerce.beans.common.WooBatchError;
import com.woocommerce.beans.order.MetadData;

/**
 * 1.1.0: bean for the wc/v3 product-variation schema, used with the nested
 * {@code products/{parentId}/variations} routes (see
 * {@link com.woocommerce.WooCommerce#getVariation}, {@code getAllVariations},
 * {@code createVariation}, {@code updateVariation}, {@code batchVariations}).
 * Variations are not a top-level {@link com.woocommerce.EndPointBaseType} because
 * their URL always needs the parent product id.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "sku",
    "status",
    "permalink",
    "price",
    "regular_price",
    "sale_price",
    "date_on_sale_from",
    "date_on_sale_from_gmt",
    "date_on_sale_to",
    "date_on_sale_to_gmt",
    "on_sale",
    "manage_stock",
    "stock_quantity",
    "stock_status",
    "weight",
    "shipping_class",
    "shipping_class_id",
    "attributes",
    "meta_data",
    "error"
})
public class WooVariation {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("status")
    private String status;
    @JsonProperty("permalink")
    private String permalink;
    @JsonProperty("price")
    private String price;
    @JsonProperty("regular_price")
    private String regularPrice;
    @JsonProperty("sale_price")
    private String salePrice;
    @JsonProperty("date_on_sale_from")
    private String dateOnSaleFrom;
    @JsonProperty("date_on_sale_from_gmt")
    private String dateOnSaleFromGmt;
    @JsonProperty("date_on_sale_to")
    private String dateOnSaleTo;
    @JsonProperty("date_on_sale_to_gmt")
    private String dateOnSaleToGmt;
    @JsonProperty("on_sale")
    private Boolean onSale;
    @JsonProperty("manage_stock")
    private Boolean manageStock;
    @JsonProperty("stock_quantity")
    private Object stockQuantity;
    @JsonProperty("stock_status")
    private String stockStatus;
    @JsonProperty("weight")
    private String weight;
    @JsonProperty("shipping_class")
    private String shippingClass;
    @JsonProperty("shipping_class_id")
    private Long shippingClassId;
    @JsonProperty("attributes")
    private List<Object> attributes = null;
    @JsonProperty("meta_data")
    private List<MetadData> metaData = null;
    // 1.1.0: see WooProduct#getError() - same per-item batch-failure shape applies
    // to products/{id}/variations/batch.
    @JsonProperty("error")
    private WooBatchError error;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("sku")
    public String getSku() {
        return sku;
    }

    @JsonProperty("sku")
    public void setSku(String sku) {
        this.sku = sku;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("permalink")
    public String getPermalink() {
        return permalink;
    }

    @JsonProperty("permalink")
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("regular_price")
    public String getRegularPrice() {
        return regularPrice;
    }

    @JsonProperty("regular_price")
    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    @JsonProperty("sale_price")
    public String getSalePrice() {
        return salePrice;
    }

    @JsonProperty("sale_price")
    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    @JsonProperty("date_on_sale_from")
    public String getDateOnSaleFrom() {
        return dateOnSaleFrom;
    }

    @JsonProperty("date_on_sale_from")
    public void setDateOnSaleFrom(String dateOnSaleFrom) {
        this.dateOnSaleFrom = dateOnSaleFrom;
    }

    @JsonProperty("date_on_sale_from_gmt")
    public String getDateOnSaleFromGmt() {
        return dateOnSaleFromGmt;
    }

    @JsonProperty("date_on_sale_from_gmt")
    public void setDateOnSaleFromGmt(String dateOnSaleFromGmt) {
        this.dateOnSaleFromGmt = dateOnSaleFromGmt;
    }

    @JsonProperty("date_on_sale_to")
    public String getDateOnSaleTo() {
        return dateOnSaleTo;
    }

    @JsonProperty("date_on_sale_to")
    public void setDateOnSaleTo(String dateOnSaleTo) {
        this.dateOnSaleTo = dateOnSaleTo;
    }

    @JsonProperty("date_on_sale_to_gmt")
    public String getDateOnSaleToGmt() {
        return dateOnSaleToGmt;
    }

    @JsonProperty("date_on_sale_to_gmt")
    public void setDateOnSaleToGmt(String dateOnSaleToGmt) {
        this.dateOnSaleToGmt = dateOnSaleToGmt;
    }

    @JsonProperty("on_sale")
    public Boolean getOnSale() {
        return onSale;
    }

    @JsonProperty("on_sale")
    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    @JsonProperty("manage_stock")
    public Boolean getManageStock() {
        return manageStock;
    }

    /**
     * 1.1.1: takes Object, not Boolean - the wc/v3 variation schema declares
     * manage_stock as boolean OR the string "parent" (variation inherits stock
     * management from its parent product), and Woo returns "parent" on reads,
     * including inside products/{id}/variations/batch responses. A Boolean-typed
     * setter made Jackson throw InvalidFormatException on every such response.
     * Non-boolean values map to null; requests still serialize a plain boolean.
     */
    @JsonProperty("manage_stock")
    public void setManageStock(Object manageStock) {
        this.manageStock = manageStock instanceof Boolean ? (Boolean) manageStock : null;
    }

    @JsonProperty("stock_quantity")
    public Object getStockQuantity() {
        return stockQuantity;
    }

    @JsonProperty("stock_quantity")
    public void setStockQuantity(Object stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @JsonProperty("stock_status")
    public String getStockStatus() {
        return stockStatus;
    }

    @JsonProperty("stock_status")
    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    @JsonProperty("weight")
    public String getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(String weight) {
        this.weight = weight;
    }

    @JsonProperty("shipping_class")
    public String getShippingClass() {
        return shippingClass;
    }

    @JsonProperty("shipping_class")
    public void setShippingClass(String shippingClass) {
        this.shippingClass = shippingClass;
    }

    @JsonProperty("shipping_class_id")
    public Long getShippingClassId() {
        return shippingClassId;
    }

    @JsonProperty("shipping_class_id")
    public void setShippingClassId(Long shippingClassId) {
        this.shippingClassId = shippingClassId;
    }

    @JsonProperty("attributes")
    public List<Object> getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(List<Object> attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("meta_data")
    public List<MetadData> getMetaData() {
        return metaData;
    }

    @JsonProperty("meta_data")
    public void setMetaData(List<MetadData> metaData) {
        this.metaData = metaData;
    }

    @JsonProperty("error")
    public WooBatchError getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(WooBatchError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
