
package com.woocommerce.beans.product;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Request payload for {@code POST products/batch}.
 *
 * <p>WooCommerce accepts up to 100 create/update/delete operations per call and
 * echoes each item back in the same three groups. For "delete" the <em>request</em>
 * is a plain list of product ids, but the <em>response</em> echoes back full product
 * objects for the deleted items - not the ids that were sent. Jackson needs one
 * field type per class, so that asymmetry can't be modeled with a single round-trip
 * class the way {@link com.woocommerce.beans.order.WooOrderBatch} does for orders
 * (where delete is a list of objects on both sides).
 *
 * <p>This class is therefore the <b>request</b> shape only ("delete" = List&lt;Long&gt;
 * ids). Deserialize the response into {@link WooProductBatchResponse} instead
 * ("delete" = List&lt;WooProduct&gt;). See {@link com.woocommerce.WooCommerceAPI#batchProducts}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "create",
    "update",
    "delete"
})
public class WooProductBatch {

    @JsonProperty("create")
    private List<WooProduct> create;
    @JsonProperty("update")
    private List<WooProduct> update;
    @JsonProperty("delete")
    private List<Long> delete;

    public WooProductBatch() {
    }

    @JsonProperty("create")
    public List<WooProduct> getCreate() {
        return create;
    }

    @JsonProperty("create")
    public void setCreate(List<WooProduct> create) {
        this.create = create;
    }

    @JsonProperty("update")
    public List<WooProduct> getUpdate() {
        return update;
    }

    @JsonProperty("update")
    public void setUpdate(List<WooProduct> update) {
        this.update = update;
    }

    @JsonProperty("delete")
    public List<Long> getDelete() {
        return delete;
    }

    @JsonProperty("delete")
    public void setDelete(List<Long> delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
