package com.woocommerce;

import java.util.List;
import java.util.Map;

/**
 * Basic interface for HTTP client
 */
public interface HttpsClient {

    /**
     * Requests url with HTTP GET and returns result object as Map
     *
     * @param url URL to request
     * @param endPointBaseType the type of the request based on enum
     * @return retrieved result
     */
    Object get(String url, EndPointBaseType endPointBaseType);

    /**
     * Requests url with HTTP GET and returns List of objects (Maps)
     *
     * @param url URL to request
     * @param requestParams params to request 
     * @param endPointBaseType the type of the request based on enum
     * @return retrieved result
     */
    List<?> getAll(String url, Map<String, String> requestParams, EndPointBaseType endPointBaseType);

    /**
     * Requests url with HTTP POST and retrieves result object as Map
     *
     * @param url to request
     * @param endPointBaseType the type of the request based on enum
     * @param object request object with will be sent as json
     * @return retrieved result
     */
    Object post(String url, EndPointBaseType endPointBaseType, Object object);

    /**
     * Requests url with HTTP PUT and retrieves result object as Map
     *
     * @param url    url to request
     * @param endPointBaseType the type of the request based on enum
     * @param object request object with will be sent as json
     * @return retrieved result
     */
    Object put(String url, EndPointBaseType endPointBaseType, Object object);

    /**
     * Requests url with HTTP DELETE and retrieves result object as Map
     *
     * @param url    url to request
     * @param endPointBaseType the type of the request based on enum
     * @return retrieved result
     */
    Object delete(String url, EndPointBaseType endPointBaseType);

    // --- 1.1.0 additions below: Class<?>-based overloads -----------------------
    //
    // Nested routes (product variations) and the products/batch response type
    // don't have - or don't want - an EndPointBaseType entry to hang their
    // response class off of (variations aren't a top-level endpoint; the
    // products/batch response is a different class than the request, see
    // WooProductBatch's javadoc). These overloads let callers pass the response
    // Class directly instead. They share implementation with the
    // EndPointBaseType-based methods above, which now just resolve
    // endPointBaseType.getClazz() and delegate here.

    /**
     * Requests url with HTTP GET and returns the parsed result.
     *
     * @param url URL to request
     * @param responseClazz class to deserialize the response into
     * @return retrieved result
     */
    Object get(String url, Class<?> responseClazz);

    /**
     * Requests url with HTTP GET and returns a List of the parsed results.
     *
     * @param url URL to request
     * @param requestParams params to request
     * @param responseClazz class to deserialize each response item into
     * @return retrieved result
     */
    List<?> getAll(String url, Map<String, String> requestParams, Class<?> responseClazz);

    /**
     * Requests url with HTTP GET and returns a parsed page plus the
     * X-WP-Total / X-WP-TotalPages response headers.
     *
     * @param url URL to request
     * @param requestParams params to request
     * @param endPointBaseType the type of the request based on enum
     * @return page of retrieved results with pagination totals
     */
    WooPage<?> getAllWithTotals(String url, Map<String, String> requestParams, EndPointBaseType endPointBaseType);

    /**
     * Requests url with HTTP GET and returns a parsed page plus the
     * X-WP-Total / X-WP-TotalPages response headers.
     *
     * @param url URL to request
     * @param requestParams params to request
     * @param responseClazz class to deserialize each response item into
     * @return page of retrieved results with pagination totals
     */
    WooPage<?> getAllWithTotals(String url, Map<String, String> requestParams, Class<?> responseClazz);

    /**
     * Requests url with HTTP POST and retrieves result object parsed as responseClazz.
     *
     * @param url to request
     * @param responseClazz class to deserialize the response into
     * @param object request object with will be sent as json
     * @return retrieved result
     */
    Object post(String url, Class<?> responseClazz, Object object);

    /**
     * Requests url with HTTP PUT and retrieves result object parsed as responseClazz.
     *
     * @param url    url to request
     * @param responseClazz class to deserialize the response into
     * @param object request object with will be sent as json
     * @return retrieved result
     */
    Object put(String url, Class<?> responseClazz, Object object);
}
