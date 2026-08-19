package com.woocommerce;

import com.woocommerce.auth.BasicAuthConfig;
import com.woocommerce.beans.order.WooOrderBatch;
import com.woocommerce.beans.product.WooProductBatch;
import com.woocommerce.beans.product.WooProductBatchResponse;
import com.woocommerce.beans.product.WooVariation;
import com.woocommerce.beans.product.WooVariationBatch;
import com.woocommerce.beans.product.WooVariationBatchResponse;
import java.util.List;
import java.util.Map;

public class WooCommerceAPI implements WooCommerce {

	private static final String API_URL_ENTITY_FORMAT = "%s/wp-json/wc/v3/%s/%d";
	private static final String API_URL_FORMAT = "%s/wp-json/wc/v3/%s";
	// 1.1.0: nested product-variation routes - these are not top-level
	// EndPointBaseType endpoints because the URL always carries the parent
	// product id.
	private static final String VARIATIONS_URL_FORMAT = "%s/wp-json/wc/v3/products/%d/variations";
	private static final String VARIATION_ENTITY_URL_FORMAT = "%s/wp-json/wc/v3/products/%d/variations/%d";
	private static final String VARIATIONS_BATCH_URL_FORMAT = "%s/wp-json/wc/v3/products/%d/variations/batch";

	private final HttpsClient client;
	String baseUrl;

	public WooCommerceAPI(BasicAuthConfig config) {
		this(config, new DefaultHttpsClient(config));
	}

	/**
	 * 1.1.0: overload that accepts an {@link HttpsClient} directly, so consumers
	 * (and this library's own tests) can inject a mock/stub instead of the real
	 * {@link DefaultHttpsClient}. The single-argument constructor above delegates
	 * here.
	 */
	public WooCommerceAPI(BasicAuthConfig config, HttpsClient client) {
		this.baseUrl = config.getUrl();
		this.client = client;
	}

	@Override
	public Object get(EndPointBaseType endPointType, int id) {
		String url = String.format(API_URL_ENTITY_FORMAT, this.baseUrl, endPointType.getValue(), id);
		return client.get(url, endPointType);
	}

	@Override
	public List<?> getAll(EndPointBaseType endPointType, Map<String, String> requestParams) {
		String url = String.format(API_URL_FORMAT, this.baseUrl, endPointType.getValue());
		return client.getAll(url, requestParams, endPointType);
	}

	@Override
	public Object update(EndPointBaseType endPointType, int id, Object object) {
		String url = String.format(API_URL_ENTITY_FORMAT, this.baseUrl, endPointType.getValue(), id);
		return client.put(url, endPointType, object);
	}

	@Override
	public Object create(EndPointBaseType endPointType, Object object) {
		String url = String.format(API_URL_FORMAT, this.baseUrl, endPointType.getValue());
		return client.post(url, endPointType, object);
	}

	@Override
	public Object delete(EndPointBaseType endPointType, int id) {
		String url = String.format(API_URL_ENTITY_FORMAT, this.baseUrl, endPointType.getValue(), id);
		return client.delete(url, endPointType);
	}

	@Override
	public Object batch(EndPointBaseType endPointType, WooOrderBatch wooOrderBatch) {
		String url = String.format(API_URL_FORMAT, this.baseUrl, endPointType.getValue());
		return client.post(url, endPointType, wooOrderBatch);
	}

	@Override
	public Object batchProducts(WooProductBatch wooProductBatch) {
		String url = String.format(API_URL_FORMAT, this.baseUrl, EndPointBaseType.PRODUCTS_BATCH.getValue());
		// Deliberately parsed as WooProductBatchResponse.class, NOT
		// EndPointBaseType.PRODUCTS_BATCH.getClazz() (which is WooProductBatch) -
		// the response's "delete" group holds full product objects, not the ids
		// the request sent. See WooProductBatch's javadoc.
		return client.post(url, WooProductBatchResponse.class, wooProductBatch);
	}

	@Override
	public WooPage<?> getAllWithTotals(EndPointBaseType endPointType, Map<String, String> params) {
		String url = String.format(API_URL_FORMAT, this.baseUrl, endPointType.getValue());
		return client.getAllWithTotals(url, params, endPointType);
	}

	@Override
	public Object getVariation(long parentId, long variationId) {
		String url = String.format(VARIATION_ENTITY_URL_FORMAT, this.baseUrl, parentId, variationId);
		return client.get(url, WooVariation.class);
	}

	@Override
	public List<?> getAllVariations(long parentId, Map<String, String> params) {
		String url = String.format(VARIATIONS_URL_FORMAT, this.baseUrl, parentId);
		return client.getAll(url, params, WooVariation.class);
	}

	@Override
	public WooPage<?> getAllVariationsWithTotals(long parentId, Map<String, String> params) {
		String url = String.format(VARIATIONS_URL_FORMAT, this.baseUrl, parentId);
		return client.getAllWithTotals(url, params, WooVariation.class);
	}

	@Override
	public Object createVariation(long parentId, WooVariation variation) {
		String url = String.format(VARIATIONS_URL_FORMAT, this.baseUrl, parentId);
		return client.post(url, WooVariation.class, variation);
	}

	@Override
	public Object updateVariation(long parentId, long variationId, WooVariation variation) {
		String url = String.format(VARIATION_ENTITY_URL_FORMAT, this.baseUrl, parentId, variationId);
		return client.put(url, WooVariation.class, variation);
	}

	@Override
	public Object batchVariations(long parentId, WooVariationBatch wooVariationBatch) {
		String url = String.format(VARIATIONS_BATCH_URL_FORMAT, this.baseUrl, parentId);
		// Same request/response split as batchProducts() above.
		return client.post(url, WooVariationBatchResponse.class, wooVariationBatch);
	}
}
