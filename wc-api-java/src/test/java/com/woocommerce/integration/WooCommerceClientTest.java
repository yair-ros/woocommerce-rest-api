package com.woocommerce.integration;

import com.woocommerce.EndPointBaseType;
import com.woocommerce.WooCommerce;
import com.woocommerce.WooCommerceAPI;
import com.woocommerce.auth.BasicAuthConfig;
import com.woocommerce.beans.order.OrderMetaDataValues;
import com.woocommerce.beans.order.OrderParamsKeys;
import com.woocommerce.beans.order.ShippingLine;
import com.woocommerce.beans.order.WooOrder;
import com.woocommerce.utils.WooOrderUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Read-only integration tests against a real WooCommerce store.
 *
 * <p>Run these tests through the repository Makefile so credentials are loaded
 * from the ignored local environment file. Normal Maven builds skip them.</p>
 */
public class WooCommerceClientTest {

    private int orderId;
    private WooCommerce wooCommerce;

    @Before
    public void setUp() {
        Assume.assumeTrue("Manual WooCommerce tests are disabled. Run them through the Makefile.",
                Boolean.parseBoolean(System.getenv("WC_RUN_MANUAL_TESTS")));

        String wcHttpsUrl = requiredEnv("WC_URL");
        String consumerKey = requiredEnv("WC_CONSUMER_KEY");
        String consumerSecret = requiredEnv("WC_CONSUMER_SECRET");
        orderId = requiredPositiveIntEnv("WC_ORDER_ID");

        BasicAuthConfig basicAuthConfig = new BasicAuthConfig(wcHttpsUrl, consumerKey, consumerSecret);
        wooCommerce = new WooCommerceAPI(basicAuthConfig);
    }

    @Test
    public void getConfiguredOrderReturnsExpectedCoreFields() {
        WooOrder order = getConfiguredOrder();

        Assert.assertEquals("WooCommerce returned a different order", Long.valueOf(orderId), order.getId());
        Assert.assertNotNull("Order status should be present", order.getStatus());
        Assert.assertNotNull("Order currency should be present", order.getCurrency());
        Assert.assertNotNull("Order total should be present", order.getTotal());
        Assert.assertNotNull("Order creation date should be present", order.getDateCreated());
    }

    @Test
    public void getConfiguredOrderDeserializesOrderCollections() {
        WooOrder order = getConfiguredOrder();

        Assert.assertNotNull("Line items should deserialize to a list", order.getLineItems());
        Assert.assertFalse("Configured order should contain at least one line item", order.getLineItems().isEmpty());
        Assert.assertNotNull("Metadata should deserialize to a list", order.getMetaData());
        Assert.assertNotNull("Shipping lines should deserialize to a list", order.getShippingLines());

        for (ShippingLine shippingLine : order.getShippingLines()) {
            Assert.assertNotNull("Shipping-line ID should be present", shippingLine.getId());
            Assert.assertNotNull("Shipping method title should be present", shippingLine.getMethodTitle());
            Assert.assertNotNull("Shipping method ID should be present", shippingLine.getMethodId());
            Assert.assertNotNull("Shipping total should be present", shippingLine.getTotal());
            Assert.assertNotNull("Shipping tax should be present", shippingLine.getTotalTax());
            Assert.assertNotNull("Shipping taxes should deserialize to a list", shippingLine.getTaxes());
        }
    }

    @Test
    public void getConfiguredOrderMetadataCanBeMapped() {
        WooOrder order = getConfiguredOrder();

        OrderMetaDataValues values = WooOrderUtils.getOrderMetaDataValues(order.getMetaData());

        Assert.assertNotNull("Order metadata conversion should return a value object", values);
    }

    @Test
    public void getAllOrdersCanFilterByConfiguredOrder() {
        Map<String, String> params = new HashMap<>();
        params.put(OrderParamsKeys.INCLUDE.getValue(), String.valueOf(orderId));
        params.put(OrderParamsKeys.PER_PAGE.getValue(), "1");

        @SuppressWarnings("unchecked")
        List<WooOrder> orders = (List<WooOrder>) wooCommerce.getAll(EndPointBaseType.ORDERS, params);

        Assert.assertNotNull("Order search should return a list", orders);
        Assert.assertEquals("Order search should return exactly the configured order", 1, orders.size());
        Assert.assertEquals("Order search returned a different order",
                Long.valueOf(orderId), orders.get(0).getId());
    }

    private WooOrder getConfiguredOrder() {
        WooOrder order = (WooOrder) wooCommerce.get(EndPointBaseType.ORDERS, orderId);
        Assert.assertNotNull("Configured order should exist and be readable", order);
        return order;
    }

    private static String requiredEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }

    private static int requiredPositiveIntEnv(String name) {
        String value = requiredEnv(name);
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue <= 0) {
                throw new IllegalStateException(name + " must be greater than zero");
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalStateException(name + " must be an integer", e);
        }
    }
}
