package com.woocommerce;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.woocommerce.auth.BasicAuthConfig;
import com.woocommerce.beans.product.WooCategory;
import com.woocommerce.beans.product.WooProduct;
import com.woocommerce.beans.product.WooProductBatch;
import com.woocommerce.beans.product.WooProductBatchResponse;
import com.woocommerce.beans.product.WooVariation;
import com.woocommerce.beans.product.WooVariationBatch;
import com.woocommerce.beans.product.WooVariationBatchResponse;

/**
 * Offline tests for the 1.1.0 API surface, backed by a local
 * {@link com.sun.net.httpserver.HttpServer} on an ephemeral port instead of a
 * real WooCommerce store. Covers: products/batch request/response shape
 * (including per-item errors), nested variation routes, the
 * PRODUCTS_CATEGORIES NPE fix, X-WP-Total/X-WP-TotalPages pagination headers,
 * the wc-api-java/1.1.0 User-Agent, and reuse of a single WooCommerceAPI/HTTP
 * client instance across sequential requests.
 */
public class WooCommerceApiMockServerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private HttpServer server;
    private WooCommerceAPI api;

    @After
    public void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void startServer(String path, HttpHandler handler) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext(path, handler);
        server.setExecutor(null);
        server.start();
        int port = server.getAddress().getPort();
        String baseUrl = "http://localhost:" + port;
        BasicAuthConfig config = new BasicAuthConfig(baseUrl, "ck_test_placeholder", "cs_test_placeholder");
        api = new WooCommerceAPI(config);
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try (InputStream is = exchange.getRequestBody()) {
            while ((length = is.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        }
        return out.toString("UTF-8");
    }

    private static void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    @Test
    public void batchProductsSendsCreateUpdateDeleteGroupsAndParsesPerItemError() throws Exception {
        AtomicReference<String> capturedBody = new AtomicReference<>();
        AtomicReference<String> capturedMethod = new AtomicReference<>();
        startServer("/wp-json/wc/v3/products/batch", exchange -> {
            capturedMethod.set(exchange.getRequestMethod());
            capturedBody.set(readBody(exchange));
            String response = "{"
                    + "\"create\":[{\"id\":101,\"name\":\"New Product\"}],"
                    + "\"update\":[{\"id\":55,\"name\":\"Updated\"}],"
                    + "\"delete\":["
                    + "  {\"id\":10,\"name\":\"Deleted\"},"
                    + "  {\"id\":11,\"error\":{\"code\":\"woocommerce_rest_product_invalid_id\",\"message\":\"Invalid ID.\"}}"
                    + "]}";
            sendJson(exchange, 200, response);
        });

        WooProduct create = new WooProduct();
        create.setName("New Product");
        WooProduct update = new WooProduct();
        update.setId(55L);
        update.setName("Updated");

        WooProductBatch batch = new WooProductBatch();
        batch.setCreate(Collections.singletonList(create));
        batch.setUpdate(Collections.singletonList(update));
        batch.setDelete(Arrays.asList(10L, 11L));

        Object result = api.batchProducts(batch);

        Assert.assertEquals("POST", capturedMethod.get());

        // Request body shape: delete must be plain ids, create/update must be full objects.
        JsonNode sentJson = mapper.readTree(capturedBody.get());
        Assert.assertEquals("New Product", sentJson.get("create").get(0).get("name").asText());
        Assert.assertEquals(55, sentJson.get("update").get(0).get("id").asInt());
        Assert.assertTrue("delete items must serialize as plain ids, not objects",
                sentJson.get("delete").get(0).isNumber());
        Assert.assertEquals(10, sentJson.get("delete").get(0).asInt());
        Assert.assertEquals(11, sentJson.get("delete").get(1).asInt());

        // Response shape: delete comes back as full product objects, one of them failed.
        Assert.assertTrue(result instanceof WooProductBatchResponse);
        WooProductBatchResponse response = (WooProductBatchResponse) result;
        Assert.assertEquals(101L, response.getCreate().get(0).getId().longValue());
        Assert.assertEquals(55L, response.getUpdate().get(0).getId().longValue());
        Assert.assertEquals(2, response.getDelete().size());
        Assert.assertEquals(10L, response.getDelete().get(0).getId().longValue());
        Assert.assertNull(response.getDelete().get(0).getError());
        Assert.assertEquals(11L, response.getDelete().get(1).getId().longValue());
        Assert.assertNotNull("failed batch item should carry an error object", response.getDelete().get(1).getError());
        Assert.assertEquals("woocommerce_rest_product_invalid_id", response.getDelete().get(1).getError().getCode());
        Assert.assertEquals("Invalid ID.", response.getDelete().get(1).getError().getMessage());
    }

    @Test
    public void updateVariationHitsNestedParentIdRoute() throws Exception {
        AtomicReference<String> capturedPath = new AtomicReference<>();
        AtomicReference<String> capturedMethod = new AtomicReference<>();
        startServer("/wp-json/wc/v3/products/123/variations/456", exchange -> {
            capturedPath.set(exchange.getRequestURI().getPath());
            capturedMethod.set(exchange.getRequestMethod());
            readBody(exchange);
            sendJson(exchange, 200, "{\"id\":456,\"sku\":\"VAR-1\",\"regular_price\":\"19.99\"}");
        });

        WooVariation variation = new WooVariation();
        variation.setSku("VAR-1");
        variation.setRegularPrice("19.99");

        Object result = api.updateVariation(123L, 456L, variation);

        Assert.assertEquals("/wp-json/wc/v3/products/123/variations/456", capturedPath.get());
        Assert.assertEquals("PUT", capturedMethod.get());
        Assert.assertTrue(result instanceof WooVariation);
        WooVariation parsed = (WooVariation) result;
        Assert.assertEquals(Long.valueOf(456L), parsed.getId());
        Assert.assertEquals("VAR-1", parsed.getSku());
    }

    @Test
    public void batchVariationsPostsToNestedBatchRouteAndParsesResponse() throws Exception {
        AtomicReference<String> capturedPath = new AtomicReference<>();
        AtomicReference<String> capturedBody = new AtomicReference<>();
        startServer("/wp-json/wc/v3/products/123/variations/batch", exchange -> {
            capturedPath.set(exchange.getRequestURI().getPath());
            capturedBody.set(readBody(exchange));
            // update[0] carries manage_stock:"parent" - the non-boolean value Woo returns for
            // variations inheriting stock management; parsing it is the 1.1.1 regression case.
            sendJson(exchange, 200, "{\"create\":[{\"id\":901,\"sku\":\"NEW-VAR\"}],"
                    + "\"update\":[{\"id\":902,\"sku\":\"UPD-VAR\",\"manage_stock\":\"parent\"}],"
                    + "\"delete\":[{\"id\":50}]}");
        });

        WooVariation create = new WooVariation();
        create.setSku("NEW-VAR");
        WooVariationBatch batch = new WooVariationBatch();
        batch.setCreate(Collections.singletonList(create));
        batch.setDelete(Collections.singletonList(50L));

        Object result = api.batchVariations(123L, batch);

        Assert.assertEquals("/wp-json/wc/v3/products/123/variations/batch", capturedPath.get());
        JsonNode sentJson = mapper.readTree(capturedBody.get());
        Assert.assertTrue(sentJson.get("delete").get(0).isNumber());

        Assert.assertTrue(result instanceof WooVariationBatchResponse);
        WooVariationBatchResponse response = (WooVariationBatchResponse) result;
        Assert.assertEquals("NEW-VAR", response.getCreate().get(0).getSku());
        Assert.assertEquals("UPD-VAR", response.getUpdate().get(0).getSku());
        Assert.assertNull(response.getUpdate().get(0).getManageStock());
        Assert.assertEquals(50L, response.getDelete().get(0).getId().longValue());
    }

    @Test
    public void getAllProductCategoriesParsesWithoutNpe() throws Exception {
        startServer("/wp-json/wc/v3/products/categories", exchange ->
                sendJson(exchange, 200, "[{\"id\":1,\"name\":\"Widgets\",\"slug\":\"widgets\"},"
                        + "{\"id\":2,\"name\":\"Gadgets\",\"slug\":\"gadgets\",\"parent\":1}]"));

        List<?> categories = api.getAll(EndPointBaseType.PRODUCTS_CATEGORIES, new HashMap<>());

        Assert.assertEquals(2, categories.size());
        Assert.assertTrue(categories.get(0) instanceof WooCategory);
        WooCategory first = (WooCategory) categories.get(0);
        Assert.assertEquals("Widgets", first.getName());
        Assert.assertEquals("widgets", first.getSlug());
        WooCategory second = (WooCategory) categories.get(1);
        Assert.assertEquals(Long.valueOf(1L), second.getParent());
    }

    @Test
    public void getAllWithTotalsReadsPaginationHeaders() throws Exception {
        startServer("/wp-json/wc/v3/products", exchange -> {
            exchange.getResponseHeaders().add("X-WP-Total", "57");
            exchange.getResponseHeaders().add("X-WP-TotalPages", "6");
            sendJson(exchange, 200, "[{\"id\":1,\"name\":\"A\"},{\"id\":2,\"name\":\"B\"}]");
        });

        WooPage<?> page = api.getAllWithTotals(EndPointBaseType.PRODUCTS, new HashMap<>());

        Assert.assertEquals(2, page.getItems().size());
        Assert.assertEquals(57, page.getTotal());
        Assert.assertEquals(6, page.getTotalPages());
        Assert.assertTrue(page.getItems().get(0) instanceof WooProduct);
    }

    @Test
    public void sendsIdentifiableUserAgent() throws Exception {
        AtomicReference<String> capturedUserAgent = new AtomicReference<>();
        startServer("/wp-json/wc/v3/products/1", exchange -> {
            capturedUserAgent.set(exchange.getRequestHeaders().getFirst("User-Agent"));
            sendJson(exchange, 200, "{\"id\":1,\"name\":\"A\"}");
        });

        api.get(EndPointBaseType.PRODUCTS, 1);

        Assert.assertEquals("wc-api-java/1.1.0", capturedUserAgent.get());
    }

    @Test
    public void sameApiInstanceHandlesSequentialRequests() throws Exception {
        AtomicInteger callCount = new AtomicInteger();
        startServer("/wp-json/wc/v3/products/1", exchange -> {
            callCount.incrementAndGet();
            sendJson(exchange, 200, "{\"id\":1,\"name\":\"A\"}");
        });

        Object first = api.get(EndPointBaseType.PRODUCTS, 1);
        Object second = api.get(EndPointBaseType.PRODUCTS, 1);

        Assert.assertNotNull(first);
        Assert.assertNotNull(second);
        Assert.assertEquals(2, callCount.get());
    }
}
