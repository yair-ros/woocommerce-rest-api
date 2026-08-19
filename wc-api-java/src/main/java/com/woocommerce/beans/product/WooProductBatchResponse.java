
package com.woocommerce.beans.product;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Response body for {@code POST products/batch}.
 *
 * <p>Counterpart to {@link WooProductBatch} (the request shape). Here "delete" is a
 * list of the full {@link WooProduct} objects that were deleted, not ids - see
 * {@link WooProductBatch}'s javadoc for why the request/response shapes are split
 * into two classes.
 *
 * <p>Any item that failed comes back with its normal fields mostly empty and an
 * {@code error} object populated instead - see {@link WooProduct#getError()} and
 * {@link com.woocommerce.beans.common.WooBatchError}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "create",
    "update",
    "delete"
})
public class WooProductBatchResponse {

    @JsonProperty("create")
    private List<WooProduct> create;
    @JsonProperty("update")
    private List<WooProduct> update;
    @JsonProperty("delete")
    private List<WooProduct> delete;

    public WooProductBatchResponse() {
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
    public List<WooProduct> getDelete() {
        return delete;
    }

    @JsonProperty("delete")
    public void setDelete(List<WooProduct> delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
