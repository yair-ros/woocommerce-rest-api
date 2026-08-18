# WooCommerce API Java Wrapper
Java wrapper for WooCommerce REST API. The library targets the wc/v3 WooCommerce
REST API, authenticated with HTTP Basic auth (consumer key/secret) over HTTPS.

## Setup
wc-api-java is available on maven central:
```xml
will update soon
```

## Usage

```java
    public static void main(String[] args) {
		// Set path and password to yours trustStore
		// (you need to make sure that you have the relevant certificate in the trusStore)
		// I suggest to use the next tool to create yours trusStore and to save on him the relevant certificate for your woocommerce:
		// http://keystore-explorer.org/
		System.setProperty("javax.net.ssl.trustStore", "C:/Users/<user>/<.keystore>");
		System.setProperty("javax.net.ssl.trustStorePassword", "<password>");

		// Setup client
		BasicAuthConfig basicAuthConfig = new BasicAuthConfig(WC_HTTPS_URL, CONSUMER_KEY, CONSUMER_SECRET);
		WooCommerceAPI wooCommerceAPI = new WooCommerceAPI(basicAuthConfig);

		// Prepare object for request
		HashMap<String, String> params = new HashMap<>();
		params.put(OrderParamsKeys.PER_PAGE.getValue() , "30");

		// Make request and retrieve result
		List<WooOrder> orders = (List<WooOrder>)wooCommerceAPI.getAll(EndPointBaseType.ORDERS, params);

		// Print the results
		int i = 1;
		for (WooOrder order : orders) {
			System.out.println("Index: " + i++ + "  ====");
			System.out.println("Id: " + order.getId());
			System.out.println("TotaL: " + order.getTotal());
			System.out.println("status:" + order.getStatus());
			System.out.println("DateCreated: " + order.getDateCreated());
			System.out.println("CreatedVia: " + order.getCreatedVia());
			System.out.println("DateCompleted: " + order.getDateCompleted());
			System.out.println("City: " + order.getShipping().getCity());
			System.out.println("Address1: " + order.getShipping().getAddress1());
			System.out.println("Address2: " + order.getShipping().getAddress2());
			System.out.println("FirstName: " + order.getShipping().getFirstName());
			System.out.println("LastName: " + order.getShipping().getLastName());
			System.out.println("PostCode: " + order.getShipping().getPostcode());
			System.out.println("CustomerNote: " + order.getCustomerNote());
			System.out.println("OrderKey: " + order.getOrderKey());
			System.out.println("PaymentMethod: " + order.getPaymentMethod());
			System.out.println("ShippingTotal: " + order.getShippingTotal());
			printLineItems(order);
			printMetaData(order);
			System.out.println(order.getCustomerUserAgent());
			System.out.println();
		}

		// Another example
		System.out.println("==============================================");
		WooOrder order = (WooOrder)wooCommerceAPI.get(EndPointBaseType.ORDERS, 31350);
		System.out.println("Id: " + order.getId());
		System.out.println("TotaL: " + order.getTotal());
		System.out.println("status:" + order.getStatus());
		System.out.println("CreatedVia: " + order.getCreatedVia());
		System.out.println("DateCompleted: " + order.getDateCompleted());
		System.out.println("City: " + order.getShipping().getCity());
		System.out.println(order.getCustomerUserAgent());
		System.out.println();
	}

	private static void printMetaData(WooOrder order)
	{
		System.out.println("---Star print Meta data---");
		List<MetadData> metadDatas = order.getMetaData();
		int j = 1;
		for (MetadData metadData : metadDatas) {
			System.out.println("Meta Data number: " + j++ + "  ====");
			System.out.print("Id: " + metadData.getId() + "  ");
			System.out.print("Key: " + metadData.getKey() + "  ");
			System.out.println("Value: " + metadData.getValue());
		}
		System.out.println("---Finish print Meta data---");
	}

	private static void printLineItems(WooOrder order) {
		System.out.println("---Star print Line items---");
		List<LineItem> lineItems = order.getLineItems();
		int j = 1;
		for (LineItem lineItem : lineItems) {
			System.out.println("Line item number: " + j++ + "  ====");
			System.out.println("Name: " + lineItem.getName());
			System.out.println("ProductId: " + lineItem.getProductId());
			System.out.println("Sku: " + lineItem.getSku());
			System.out.println("Quantity: " + lineItem.getQuantity());
			System.out.println("Price: " + lineItem.getPrice());
			System.out.println("Subtotal: " + lineItem.getSubtotal());
			System.out.println("Total: " + lineItem.getTotal());
			System.out.println("VariationId: " + lineItem.getVariationId());
		}
		System.out.println("---Finish print Line items---");
	}
}
```
