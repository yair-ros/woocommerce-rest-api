
package com.woocommerce.beans.product;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Response body for {@code POST products/{parentId}/variations/batch}. Counterpart
 * to {@link WooVariationBatch} (the request shape) - see its javadoc.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "create",
    "update",
    "delete"
})
public class WooVariationBatchResponse {

    @JsonProperty("create")
    private List<WooVariation> create;
    @JsonProperty("update")
    private List<WooVariation> update;
    @JsonProperty("delete")
    private List<WooVariation> delete;

    public WooVariationBatchResponse() {
    }

    @JsonProperty("create")
    public List<WooVariation> getCreate() {
        return create;
    }

    @JsonProperty("create")
    public void setCreate(List<WooVariation> create) {
        this.create = create;
    }

    @JsonProperty("update")
    public List<WooVariation> getUpdate() {
        return update;
    }

    @JsonProperty("update")
    public void setUpdate(List<WooVariation> update) {
        this.update = update;
    }

    @JsonProperty("delete")
    public List<WooVariation> getDelete() {
        return delete;
    }

    @JsonProperty("delete")
    public void setDelete(List<WooVariation> delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
