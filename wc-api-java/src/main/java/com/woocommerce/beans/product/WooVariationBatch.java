
package com.woocommerce.beans.product;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Request payload for {@code POST products/{parentId}/variations/batch}.
 *
 * <p>Same request/response split as {@link WooProductBatch}: "delete" here is a
 * list of variation ids. The response ("delete" = list of {@link WooVariation})
 * should be deserialized into {@link WooVariationBatchResponse} instead.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "create",
    "update",
    "delete"
})
public class WooVariationBatch {

    @JsonProperty("create")
    private List<WooVariation> create;
    @JsonProperty("update")
    private List<WooVariation> update;
    @JsonProperty("delete")
    private List<Long> delete;

    public WooVariationBatch() {
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
