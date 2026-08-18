package com.woocommerce.utils;

import com.woocommerce.beans.order.MetadData;
import com.woocommerce.beans.order.OrderMetaDataValues;
import java.util.List;

public class WooOrderUtils {

    public static OrderMetaDataValues getOrderMetaDataValues(List<MetadData> metaDataList) {
        OrderMetaDataValues orderMetaDataValues = new OrderMetaDataValues();
        for (MetadData metadData : metaDataList) {
            Object value = metadData.getValue();
            switch (metadData.getKey())	{
                case "is_vat_exempt":
                    String isVatExempt = (String) value;
                    orderMetaDataValues.setIsVatExempt(isVatExempt != null ? isVatExempt : "");
                    break;
                case "_wc_facebook_for_woocommerce_order_placed":
                    orderMetaDataValues.setWcFacebookForWoocommerceOrderPlaced((String)value);
                    break;
                case "_wc_google_analytics_pro_identity":
                    orderMetaDataValues.setWcGoogleAnalyticsProIdentity((String)value);
                    break;
                case "_wc_google_analytics_pro_placed":
                    orderMetaDataValues.setWcGoogleAnalyticsProPlaced((String)value);
                    break;
                case "_cgUniqueID":
                    orderMetaDataValues.setCgUniqueID((String)value);
                    break;
                case "_cardToken":
                    String cardToken = (String) value;
                    orderMetaDataValues.setCardToken(cardToken != null ? cardToken : "");
                    break;
                case "_firstPayment":
                    String firstPayment = (String) value;
                    orderMetaDataValues.setFirstPayment(firstPayment != null ? firstPayment : "");
                    break;
                case "_periodicalPayment":
                    String periodicalPayment = (String) value;
                    orderMetaDataValues.setPeriodicalPayment(periodicalPayment != null ? periodicalPayment : "");
                    break;
                case "_cardExp":
                    String cardExp = (String) value;
                    orderMetaDataValues.setCardExp(cardExp != null ? cardExp : "");
                    break;
                case "_authNumber":
                    String authNumber = (String) value;
                    orderMetaDataValues.setAuthNumber(authNumber !=null ? authNumber : "");
                    break;
                case "_numberOfPayments":
                    String numberOfPayments = (String) value;
                    orderMetaDataValues.setNumberOfPayments(numberOfPayments != null ? numberOfPayments : "");
                    break;
                case "Customer Id":
                    String customerId = (String) value;
                    orderMetaDataValues.setCustomerId(customerId != null ? customerId : "");
                    break;
                case "_directpay_charge_captured":
                    orderMetaDataValues.setDirectpayChargeCaptured((String)value);
                    break;
                case "_pys_purchase_event_fired":
                    orderMetaDataValues.setPysPurchaseEventFired((String) value);
                    break;
                case "billing_business_invoice":
                    Boolean isBillingBusinessInvoice = value != null && "1".equals(value.toString());
                    orderMetaDataValues.setIsBillingBusinessInvoice(isBillingBusinessInvoice);
                    break;
                case "billing_invoice_name":
                    orderMetaDataValues.setBillingInvoiceName((String) value);
                    break;
                case "billing_invoice_id_type":
                    if (value == null) break;
                    try {
                        orderMetaDataValues.setBillingInvoiceIdType(Long.parseLong(value.toString()));
                    } catch (NumberFormatException e) {
                        orderMetaDataValues.setBillingInvoiceIdType(null);
                    }
                    break;
                default:
                    String errorMessage = "There is a new woo order metadata key. 'meta key': " + metadData.getKey() + ".   'meta value': " + value;
                    System.out.println(errorMessage);
            }
        }
        return orderMetaDataValues;
    }
}
