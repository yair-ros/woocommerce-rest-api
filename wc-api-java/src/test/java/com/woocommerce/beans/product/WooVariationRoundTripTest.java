package com.woocommerce.beans.product;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Pure Jackson tests for the 1.1.1 manage_stock fix on {@link WooVariation}: the
 * wc/v3 variation schema declares manage_stock as boolean OR the string "parent"
 * (variation inherits stock management from its parent product). A Boolean-typed
 * setter made every batch response containing "parent" throw
 * InvalidFormatException. No HTTP involved.
 */
public class WooVariationRoundTripTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void manageStockParentStringDeserializesToNull() throws Exception {
        WooVariation parsed = mapper.readValue(
                "{\"id\":902,\"sku\":\"VAR-1\",\"manage_stock\":\"parent\"}", WooVariation.class);
        Assert.assertEquals(Long.valueOf(902L), parsed.getId());
        Assert.assertNull(parsed.getManageStock());
    }

    @Test
    public void manageStockBooleanRoundTrips() throws Exception {
        WooVariation trueParsed = mapper.readValue("{\"id\":1,\"manage_stock\":true}", WooVariation.class);
        Assert.assertEquals(Boolean.TRUE, trueParsed.getManageStock());

        WooVariation falseParsed = mapper.readValue("{\"id\":2,\"manage_stock\":false}", WooVariation.class);
        Assert.assertEquals(Boolean.FALSE, falseParsed.getManageStock());
    }

    @Test
    public void manageStockSerializesAsPlainBooleanOnRequests() throws Exception {
        WooVariation variation = new WooVariation();
        variation.setId(3L);
        variation.setManageStock(true);
        Assert.assertTrue(mapper.writeValueAsString(variation).contains("\"manage_stock\":true"));

        variation.setManageStock(false);
        Assert.assertTrue(mapper.writeValueAsString(variation).contains("\"manage_stock\":false"));

        // NON_NULL keeps an unset manage_stock out of the request entirely.
        WooVariation unset = new WooVariation();
        unset.setId(4L);
        Assert.assertFalse(mapper.writeValueAsString(unset).contains("manage_stock"));
    }
}
