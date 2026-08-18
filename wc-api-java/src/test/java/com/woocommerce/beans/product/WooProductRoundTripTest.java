package com.woocommerce.beans.product;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woocommerce.beans.order.MetadData;

/**
 * Pure Jackson round-trip tests for the 1.1.0 additions to {@link WooProduct}:
 * meta_data, stock_status, the *_gmt sale-date fields, shipping_class(_id), and
 * the batch-response-only "error" field. No HTTP involved.
 */
public class WooProductRoundTripTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void roundTripsNewOneDotOneZeroFields() throws Exception {
        WooProduct product = new WooProduct();
        product.setId(42L);
        product.setName("Test Product");
        product.setSku("SKU-42");
        product.setStockStatus("onbackorder");
        product.setDateOnSaleFrom("2026-01-01T00:00:00");
        product.setDateOnSaleFromGmt("2026-01-01T00:00:00");
        product.setDateOnSaleTo("2026-02-01T00:00:00");
        product.setDateOnSaleToGmt("2026-02-01T00:00:00");
        product.setShippingClass("bulky");
        product.setShippingClassId(7L);

        MetadData meta = new MetadData();
        meta.setKey("_custom_field");
        meta.setValue("custom-value");
        product.setMetaData(Collections.singletonList(meta));

        String json = mapper.writeValueAsString(product);
        WooProduct parsed = mapper.readValue(json, WooProduct.class);

        Assert.assertEquals(product.getId(), parsed.getId());
        Assert.assertEquals("SKU-42", parsed.getSku());
        Assert.assertEquals("onbackorder", parsed.getStockStatus());
        Assert.assertEquals("2026-01-01T00:00:00", parsed.getDateOnSaleFrom());
        Assert.assertEquals("2026-01-01T00:00:00", parsed.getDateOnSaleFromGmt());
        Assert.assertEquals("2026-02-01T00:00:00", parsed.getDateOnSaleTo());
        Assert.assertEquals("2026-02-01T00:00:00", parsed.getDateOnSaleToGmt());
        Assert.assertEquals("bulky", parsed.getShippingClass());
        Assert.assertEquals(Long.valueOf(7L), parsed.getShippingClassId());
        Assert.assertNotNull(parsed.getMetaData());
        Assert.assertEquals(1, parsed.getMetaData().size());
        Assert.assertEquals("_custom_field", parsed.getMetaData().get(0).getKey());
        Assert.assertEquals("custom-value", parsed.getMetaData().get(0).getValue());
    }

    @Test
    public void batchResponseErrorFieldDeserializesButIsNeverSent() throws Exception {
        String json = "{\"id\":99,\"error\":{\"code\":\"woocommerce_rest_product_invalid_id\",\"message\":\"Invalid ID.\"}}";
        WooProduct parsed = mapper.readValue(json, WooProduct.class);

        Assert.assertNotNull(parsed.getError());
        Assert.assertEquals("woocommerce_rest_product_invalid_id", parsed.getError().getCode());
        Assert.assertEquals("Invalid ID.", parsed.getError().getMessage());

        // error must never be serialized back out on requests we send (it's a
        // response-only, batch-failure field - JsonInclude.NON_NULL keeps it out
        // as long as nothing sets it).
        WooProduct clean = new WooProduct();
        clean.setId(1L);
        String cleanJson = mapper.writeValueAsString(clean);
        Assert.assertFalse("error field must be omitted when null: " + cleanJson, cleanJson.contains("\"error\""));
    }

    @Test
    public void unknownServerFieldsDoNotBreakDeserialization() throws Exception {
        String json = "{\"id\":1,\"name\":\"A\",\"some_future_field\":\"value\"}";
        WooProduct parsed = mapper.readValue(json, WooProduct.class);
        Assert.assertEquals(Long.valueOf(1L), parsed.getId());
        Assert.assertEquals("value", parsed.getAdditionalProperties().get("some_future_field"));
    }
}
