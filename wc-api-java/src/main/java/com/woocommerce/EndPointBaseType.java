package com.woocommerce;

import com.woocommerce.beans.coupon.WooCoupon;
import com.woocommerce.beans.customer.WooCustomer;
import com.woocommerce.beans.order.WooOrder;
import com.woocommerce.beans.order.WooOrderBatch;
import com.woocommerce.beans.product.WooCategory;
import com.woocommerce.beans.product.WooProduct;
import com.woocommerce.beans.product.WooProductBatch;
import com.woocommerce.beans.shipping.WooShippingClasses;

/**
 * Enum with basic WooCommerce endpoints
 */
public enum EndPointBaseType {

    COUPONS("coupons", WooCoupon.class),
    CUSTOMERS("customers", WooCustomer.class),
    ORDERS("orders", WooOrder.class),
    ORDERS_BATCH("orders/batch", WooOrderBatch.class),
    PRODUCTS("products", WooProduct.class),
    // 1.1.0: clazz kept in sync with the ORDERS_BATCH pattern above for
    // symmetry, but the response body is NOT actually WooProductBatch - see
    // WooCommerceAPI#batchProducts()/WooProductBatch's javadoc for why the
    // request and response shapes had to be split into two classes.
    PRODUCTS_BATCH("products/batch", WooProductBatch.class),
    PRODUCTS_ATTRIBUTES("products/attributes", null),
    PRODUCTS_CATEGORIES("products/categories", WooCategory.class),
    PRODUCTS_SHIPPING_CLASSES("products/shipping_classes", WooShippingClasses.class),
    PRODUCTS_TAGS("products/tags", null),
    REPORTS("reports", null),
    REPORTS_SALES("reports/sales", null),
    REPORTS_TOP_SELLERS("reports/top_sellers", null),
    TAXES("taxes", null),
    TAXES_CLASSES("taxes/classes", null),
    WEBHOOKS("webhooks", null);

    private final String value;
    private final Class<?> clazz;

    EndPointBaseType(String value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }

    public String getValue() {
        return value;
    }
    
    public Class<?> getClazz() {
        return clazz;
    }
}
