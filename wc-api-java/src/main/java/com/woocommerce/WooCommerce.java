package com.woocommerce;

import java.util.List;
import java.util.Map;

import com.woocommerce.beans.order.WooOrderBatch;
import com.woocommerce.beans.product.WooProductBatch;
import com.woocommerce.beans.product.WooVariation;
import com.woocommerce.beans.product.WooVariationBatch;

/**
 * Main interface for WooCommerce REST API
 */
public interface WooCommerce {

    /**
     * Creates WooCommerce entity
     *
     * @param endPointBaseType API endPoint base @see {@link EndPointBaseType}
     * @param object       Map with entity properties and values
     * @return Map with created entity
     */
    Object create(EndPointBaseType endPointBaseType, Object object);

    /**
     * Retrieves on WooCommerce entity
     *
     * @param endPointBaseType API endPoint base @see {@link EndPointBaseType}
     * @param id id of WooCommerce entity
     * @return Retrieved WooCommerce entity
     */
    Object get(EndPointBaseType endPointBaseType, int id);

    /**
     * Retrieves all WooCommerce entities
     *
     * @param endPointBaseType API endPoint base @see {@link EndPointBaseType}
     * @param params API Map (key=OrderParamsKeys, value=String) of params that will concatenate to the request, @see OrderParamsKeys
     * @return List of retrieved entities
     */
	List<?> getAll(EndPointBaseType endPointBaseType, Map<String, String> params);

    /**
     * Updates WooCommerce entity
     *
     * @param endPointBaseType API endPoint base @see {@link EndPointBaseType}
     * @param id           id of the entity to update
     * @param object       Map with updated properties
     * @return updated WooCommerce entity
     */
    Object update(EndPointBaseType endPointBaseType, int id, Object object);

    /**
     * Deletes WooCommerce entity
     *
     * @param endPointBaseType API endPoint base @see {@link EndPointBaseType}
     * @param id           id of the entity to update
     * @return deleted WooCommerce entity
     */
    Object delete(EndPointBaseType endPointBaseType, int id);
    
    /**
     * Create\Update\Delete batch of WooCommerce entity
     *
     * @param endPointBaseType API endPoint base @see {@link EndPointBaseType}
     * @param wooOrderBatch entity properties and values @see {@link WooOrderBatch}
     * @return Object with created entity
     */
    Object batch(EndPointBaseType endPointBaseType, WooOrderBatch wooOrderBatch);

    // --- 1.1.0 additions below -------------------------------------------------

    /**
     * Create\Update\Delete batch of products via POST products/batch.
     *
     * <p>Kept as a separate method rather than overloading {@link #batch} because
     * {@link #batch}'s signature is pinned to {@link WooOrderBatch} - it's existing
     * public API already relied on by consumers and must not change. The response
     * is a {@link com.woocommerce.beans.product.WooProductBatchResponse}, whose
     * "delete" group holds the full deleted product objects WooCommerce echoes
     * back (not the ids that were sent) - see that class's javadoc.
     *
     * @param wooProductBatch create/update/delete groups @see {@link WooProductBatch}
     * @return response with created/updated/deleted entities (and any per-item errors)
     */
    Object batchProducts(WooProductBatch wooProductBatch);

    /**
     * Retrieves all WooCommerce entities together with the X-WP-Total /
     * X-WP-TotalPages pagination headers WooCommerce sets on list responses.
     * The plain {@link #getAll} is untouched by this addition.
     *
     * @param endPointBaseType API endPoint base @see {@link EndPointBaseType}
     * @param params API Map (key=OrderParamsKeys, value=String) of params that will concatenate to the request
     * @return page of retrieved entities plus total/totalPages
     */
    WooPage<?> getAllWithTotals(EndPointBaseType endPointBaseType, Map<String, String> params);

    /**
     * Retrieves a single product variation via the nested
     * products/{parentId}/variations/{variationId} route.
     *
     * @param parentId id of the parent (variable) product
     * @param variationId id of the variation
     * @return retrieved variation
     */
    Object getVariation(long parentId, long variationId);

    /**
     * Retrieves all variations of a product via products/{parentId}/variations.
     *
     * @param parentId id of the parent (variable) product
     * @param params API params that will concatenate to the request
     * @return list of retrieved variations
     */
    List<?> getAllVariations(long parentId, Map<String, String> params);

    /**
     * Same as {@link #getAllVariations} but also returns the X-WP-Total /
     * X-WP-TotalPages pagination headers.
     *
     * @param parentId id of the parent (variable) product
     * @param params API params that will concatenate to the request
     * @return page of retrieved variations plus total/totalPages
     */
    WooPage<?> getAllVariationsWithTotals(long parentId, Map<String, String> params);

    /**
     * Creates a product variation via POST products/{parentId}/variations.
     *
     * @param parentId id of the parent (variable) product
     * @param variation variation properties and values
     * @return created variation
     */
    Object createVariation(long parentId, WooVariation variation);

    /**
     * Updates a product variation via PUT products/{parentId}/variations/{variationId}.
     *
     * @param parentId id of the parent (variable) product
     * @param variationId id of the variation to update
     * @param variation updated variation properties
     * @return updated variation
     */
    Object updateVariation(long parentId, long variationId, WooVariation variation);

    /**
     * Create\Update\Delete batch of variations of one product via
     * POST products/{parentId}/variations/batch.
     *
     * @param parentId id of the parent (variable) product
     * @param wooVariationBatch create/update/delete groups @see {@link WooVariationBatch}
     * @return response with created/updated/deleted variations (and any per-item errors)
     */
    Object batchVariations(long parentId, WooVariationBatch wooVariationBatch);

}
