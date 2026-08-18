package com.woocommerce.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.woocommerce.beans.order.MetadData;
import com.woocommerce.beans.order.OrderMetaDataValues;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import org.junit.Test;

public class WooOrderUtilsTest {

    @Test
    public void nullMetadataListReturnsEmptyValues() {
        OrderMetaDataValues values = WooOrderUtils.getOrderMetaDataValues(null);

        assertNotNull(values);
        assertNull(values.getIsVatExempt());
        assertNull(values.getBillingInvoiceName());
    }

    @Test
    public void nullEntriesAndNullKeysAreIgnored() {
        MetadData nullKey = metadata(null, "ignored");
        MetadData known = metadata("is_vat_exempt", "yes");

        OrderMetaDataValues values = WooOrderUtils.getOrderMetaDataValues(
                Arrays.asList(null, nullKey, known));

        assertEquals("yes", values.getIsVatExempt());
    }

    @Test
    public void unknownMetadataIsIgnoredWithoutWritingToStdout() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        PrintStream capturedOut = new PrintStream(output);

        try {
            System.setOut(capturedOut);
            OrderMetaDataValues values = WooOrderUtils.getOrderMetaDataValues(
                    Arrays.asList(metadata("plugin_specific_key", "sensitive value")));

            assertNotNull(values);
            assertEquals(0, output.size());
        } finally {
            System.setOut(originalOut);
            capturedOut.close();
        }
    }

    @Test
    public void recognizedMetadataIsMapped() {
        OrderMetaDataValues values = WooOrderUtils.getOrderMetaDataValues(Arrays.asList(
                metadata("billing_business_invoice", "1"),
                metadata("billing_invoice_name", "Example Ltd"),
                metadata("billing_invoice_id_type", "42")));

        assertEquals(Boolean.TRUE, values.getIsBillingBusinessInvoice());
        assertEquals("Example Ltd", values.getBillingInvoiceName());
        assertEquals(Long.valueOf(42), values.getBillingInvoiceIdType());
    }

    private static MetadData metadata(String key, Object value) {
        MetadData metadata = new MetadData();
        metadata.setKey(key);
        metadata.setValue(value);
        return metadata;
    }
}
