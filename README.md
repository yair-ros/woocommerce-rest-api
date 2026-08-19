# WooCommerce API Java Wrapper
Java wrapper for WooCommerce REST API. The library targets the wc/v3 WooCommerce
REST API, authenticated with HTTP Basic auth (consumer key/secret) over HTTPS.

## Setup
wc-api-java is available on maven central:
```xml
will update soon
```

## Running the manual WooCommerce integration tests

`WooCommerceClientTest` connects to a real WooCommerce store. Its URL,
credentials, and test order ID are loaded from a local environment file that is
ignored by Git.

From the repository root, create the local file:

```shell
make woocommerce-test-env
```

Edit `.woocommerce-test.env` and replace all placeholder values:

```dotenv
WC_URL=https://your-store.example/
WC_CONSUMER_KEY=ck_replace_me
WC_CONSUMER_SECRET=cs_replace_me
WC_ORDER_ID=12345
```

Run the complete read-only integration suite:

```shell
make woocommerce-test
```

Run only one test method when needed:

```shell
make woocommerce-test WC_TEST=getConfiguredOrderMetadataCanBeMapped
```

The Make target sets `WC_RUN_MANUAL_TESTS=true`. Without that explicit gate,
the integration tests are skipped during normal Maven test runs. Never add
`.woocommerce-test.env` to Git; only `.woocommerce-test.env.example` should be
committed.

The suite verifies that the configured order can be retrieved, its core fields
and collections deserialize correctly, its metadata can be mapped, and order
listing can filter by its ID. The tests perform only `GET` requests; they do not
create, update, or delete WooCommerce data.

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
      BasicAuthConfig basicAuthConfig = new BasicAuthConfig("http://woocommerce.com", "consumerKey", "consumerSecret");
      WooCommerceAPI wooCommerceAPI = new WooCommerceAPI(basicAuthConfig);
		
		// Prepare object for request
		HashMap<OrderParamsKeys, String> params = new HashMap<>();
		params.put(OrderParamsKeys.PER_PAGE , "30");

		// Make request and retrieve result		
		List<WooOrder> orders = (List<WooOrder>)wooCommerceAPI.getAll(EndpointBaseType.ORDERS, params);
		
		// Print the results
		int i = 1;
		for (WooOrder order : orders) {
			System.out.println("Index: " + i++ + "  ====");
			System.out.println("Id: " + order.getId());
			System.out.println("TotaL: " + order.getTotal());
			System.out.println("status:" + order.getStatus());
			System.out.println("CreatedVia: " + order.getCreatedVia());
			System.out.println("DateCompleted: " + order.getDateCompleted());
			System.out.println("City: " + order.getShipping().getCity());
			System.out.println(order.getCustomerUserAgent());
			System.out.println();
		}

		// Another example
		System.out.println("==============================================");
		WooOrder order = (WooOrder)wooCommerceAPI.get(EndpointBaseType.ORDERS, 31350);
		System.out.println("Id: " + order.getId());
		System.out.println("TotaL: " + order.getTotal());
		System.out.println("status:" + order.getStatus());
		System.out.println("CreatedVia: " + order.getCreatedVia());
		System.out.println("DateCompleted: " + order.getDateCompleted());
		System.out.println("City: " + order.getShipping().getCity());
		System.out.println(order.getCustomerUserAgent());
		System.out.println();
	}
```
