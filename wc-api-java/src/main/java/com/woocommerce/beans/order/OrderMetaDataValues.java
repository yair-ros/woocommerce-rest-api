package com.woocommerce.beans.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created on 14/07/2020.
 */
@Getter
@Setter
@ToString
public class OrderMetaDataValues
{
    //is_vat_exempt
    private String isVatExempt;
    //_wc_facebook_for_woocommerce_order_placed
    private String wcFacebookForWoocommerceOrderPlaced;
    //_wc_google_analytics_pro_identity
    private String wcGoogleAnalyticsProIdentity;
    //_wc_google_analytics_pro_placed
    private String wcGoogleAnalyticsProPlaced;
    //_cgUniqueID
    private String cgUniqueID;
    //_cardToken
    private String cardToken;
    //_firstPayment
    private String firstPayment;
    //_periodicalPayment
    private String periodicalPayment;
    //_cardExp
    private String cardExp;
    //_authNumber
    private String authNumber;
    //_numberOfPayments
    private String numberOfPayments;
    //Customer Id
    private String customerId;
    //_directpay_charge_captured
    private String directpayChargeCaptured;
    //_pys_purchase_event_fired
    private String pysPurchaseEventFired;
    // billing_invoice_id_type
    private Long billingInvoiceIdType;
    // billing_business_invoice
    private Boolean isBillingBusinessInvoice;
    // billing_invoice_name
    private String billingInvoiceName;
}
