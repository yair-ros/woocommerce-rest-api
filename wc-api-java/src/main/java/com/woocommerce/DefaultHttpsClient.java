package com.woocommerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woocommerce.auth.AuthParamsKey;
import com.woocommerce.auth.BasicAuthConfig;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

public class DefaultHttpsClient implements HttpsClient, Closeable {

	/**
	 * 1.1.0: Cloudflare sits in front of the WooCommerce store and 403-blocks
	 * requests carrying suspicious User-Agent strings (e.g. Python-urllib's
	 * default). Apache HttpClient's own default UA happens to pass today, but an
	 * explicit, identifiable UA is more robust than depending on that.
	 */
	static final String USER_AGENT = "wc-api-java/1.1.0";

	/**
	 * 1.1.2: how many leading bytes of a 2xx body are kept aside for diagnostics
	 * in case JSON parsing fails. WordPress can serve a non-JSON body with a 200
	 * (a PHP warning prepended to the output, an HTML page from a plugin/host):
	 * previously the body was consumed by the failed parse and lost, leaving only
	 * "Unexpected character '<'" in the log. Parsing still streams into Jackson;
	 * the full body is never buffered.
	 */
	static final int RESPONSE_HEAD_CAPTURE_BYTES = 2048;

	private final BasicAuthConfig config;
	private final RequestConfig requestConfig;
	// 1.1.0: previously a new CloseableHttpClient (and therefore a fresh TCP+TLS
	// handshake) was built per request. A single pooled client is now built once
	// in the constructor and reused for the lifetime of this instance.
	private final CloseableHttpClient httpClient;

	public DefaultHttpsClient(BasicAuthConfig config) {
		this.config = config;
		this.requestConfig = RequestConfig.custom()
				.setConnectTimeout(config.getConnectTimeoutMillis())
				.setConnectionRequestTimeout(config.getConnectTimeoutMillis())
				.setSocketTimeout(config.getSocketTimeoutMillis())
				.build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(50);
		connectionManager.setDefaultMaxPerRoute(20);
		this.httpClient = HttpClientBuilder.create()
				.setConnectionManager(connectionManager)
				.setDefaultRequestConfig(requestConfig)
				.setUserAgent(USER_AGENT)
				.build();
	}

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public Object get(String url, EndPointBaseType endPointBaseType) {
		return get(url, endPointBaseType.getClazz());
	}

	@Override
	public Object get(String url, Class<?> responseClazz) {
		HttpGet httpGet = new HttpGet(buildURI(url));
		return doHttpRequest(responseClazz, httpGet);
	}

	@Override
	public List<?> getAll(String url, Map<String, String> params, EndPointBaseType endPointBaseType) {
		return getAll(url, params, endPointBaseType.getClazz());
	}

	@Override
	public List<?> getAll(String url, Map<String, String> params, Class<?> responseClazz) {
		HttpGet httpGet = new HttpGet(buildURI(url, params));
		return doHttpRequest(responseClazz, httpGet, true);
	}

	@Override
	public WooPage<?> getAllWithTotals(String url, Map<String, String> params, EndPointBaseType endPointBaseType) {
		return getAllWithTotals(url, params, endPointBaseType.getClazz());
	}

	@Override
	public WooPage<?> getAllWithTotals(String url, Map<String, String> params, Class<?> responseClazz) {
		HttpGet httpGet = new HttpGet(buildURI(url, params));
		return doHttpRequestWithTotals(responseClazz, httpGet);
	}

	@Override
	public Object put(String url, EndPointBaseType endPointBaseType, Object object) {
		return put(url, endPointBaseType.getClazz(), object);
	}

	@Override
	public Object put(String url, Class<?> responseClazz, Object object) {
		HttpPut httpPut = new HttpPut(buildURI(url));
		httpPut.setEntity(convertObjectToJsonHttpEntity(object));
		return doHttpRequest(responseClazz, httpPut);
	}

	@Override
	public Object post(String url, EndPointBaseType endPointBaseType, Object object) {
		return post(url, endPointBaseType.getClazz(), object);
	}

	@Override
	public Object post(String url, Class<?> responseClazz, Object object) {
		HttpPost httpPost = new HttpPost(buildURI(url));
		httpPost.setEntity(convertObjectToJsonHttpEntity(object));
		return doHttpRequest(responseClazz, httpPost);
	}

	@Override
	public Object delete(String url, EndPointBaseType endPointBaseType) {
		HttpDelete httpDelete = new HttpDelete(buildURI(url));
		return doHttpRequest(endPointBaseType.getClazz(), httpDelete);
	}

	/** Releases the pooled connections. Safe to skip if the process is exiting anyway. */
	@Override
	public void close() throws IOException {
		httpClient.close();
	}

	private <T> T doHttpRequest(Class<?> responseClazz, HttpRequestBase httpRequest) {
		return doHttpRequest(responseClazz, httpRequest, false);
	}

    private <T> T doHttpRequest(Class<?> responseClazz, HttpRequestBase httpRequest, boolean isList) {
        applyCommonHeaders(httpRequest);
        try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {

            int statusCode = response.getStatusLine().getStatusCode();
            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            HttpEntity entity = response.getEntity();
            if (entity == null) return null;

            try (InputStream inputStream = entity.getContent()) {
                if (statusCode >= 200 && statusCode < 300) {
                    return parseSuccessResponse(responseClazz, isList, inputStream, statusCode, reasonPhrase);
                } else {
                    throw buildHttpRequestException(statusCode, reasonPhrase, inputStream);
                }
            }
        } catch (IOException e) {
            throw ioErrorAsRuntimeException(e, httpRequest);
        }
    }

    private WooPage<?> doHttpRequestWithTotals(Class<?> responseClazz, HttpRequestBase httpRequest) {
        applyCommonHeaders(httpRequest);
        try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {

            int statusCode = response.getStatusLine().getStatusCode();
            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            int total = parseIntHeader(response.getFirstHeader("X-WP-Total"));
            int totalPages = parseIntHeader(response.getFirstHeader("X-WP-TotalPages"));
            HttpEntity entity = response.getEntity();
            if (entity == null) return new WooPage<>(new ArrayList<>(), total, totalPages);

            try (InputStream inputStream = entity.getContent()) {
                if (statusCode >= 200 && statusCode < 300) {
                    List<?> items = parseSuccessResponse(responseClazz, true, inputStream, statusCode, reasonPhrase);
                    return new WooPage<>(items, total, totalPages);
                } else {
                    throw buildHttpRequestException(statusCode, reasonPhrase, inputStream);
                }
            }
        } catch (IOException e) {
            throw ioErrorAsRuntimeException(e, httpRequest);
        }
    }

    private void applyCommonHeaders(HttpRequestBase httpRequest) {
        httpRequest.setHeader("Content-Type", "application/json");
        // 1.0.6: credentials travel as an HTTP Basic Authorization header by
        // default instead of consumer_key/consumer_secret query parameters.
        // Query-param auth wrote the live credentials into every server
        // access log, proxy log, and any exception message carrying the URL.
        // The header goes over TLS only (this client is https-only) and never
        // appears in URLs. Legacy query-param mode remains available via
        // BasicAuthConfig.useQueryStringAuth for hosts that strip the
        // Authorization header (some Apache CGI/FastCGI setups).
        if (!config.isUseQueryStringAuth()) {
            String credentials = config.getConsumerKey() + ":" + config.getConsumerSecret();
            String encoded = java.util.Base64.getEncoder()
                    .encodeToString(credentials.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            httpRequest.setHeader("Authorization", "Basic " + encoded);
        }
    }

    private HttpRequestException buildHttpRequestException(int statusCode, String reasonPhrase, InputStream inputStream)
            throws IOException {
        String responseBody = inputStreamToString(inputStream);
        String errorCode = null;
        String errorMessage = null;
        try {
            Map<?, ?> map = mapper.readValue(responseBody, Map.class);
            errorCode = map.get("code") != null ? map.get("code").toString() : null;
            errorMessage = map.get("message") != null ? map.get("message").toString() : null;
        } catch (Exception ignore) {
            // Not JSON → ignore
        }
        return new HttpRequestException(statusCode, reasonPhrase, responseBody, errorCode, errorMessage);
    }

    private RuntimeException ioErrorAsRuntimeException(IOException e, HttpRequestBase httpRequest) {
        // Redact credential query params from the URI before it can reach
        // logs/emails via the exception message (the 2026-08-17 incident:
        // a consumer-side error email carried the live credentials).
        return new RuntimeException("IO error during request to " + redactCredentials(httpRequest.getURI()), e);
    }

    private static int parseIntHeader(Header header) {
        if (header == null || header.getValue() == null) return -1;
        try {
            return Integer.parseInt(header.getValue().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** consumer_key/consumer_secret values replaced with [REDACTED]; only relevant in legacy query-param auth mode. */
    static String redactCredentials(URI uri) {
        if (uri == null) return "";
        return uri.toString()
                .replaceAll("(?i)(consumer_key=)[^&\\s]+", "$1[REDACTED]")
                .replaceAll("(?i)(consumer_secret=)[^&\\s]+", "$1[REDACTED]");
    }

    /**
     * 1.1.2: a 2xx body that Jackson cannot parse is reported as an
     * {@link HttpRequestException} carrying the status and the first
     * {@link #RESPONSE_HEAD_CAPTURE_BYTES} of the body, so the server's actual
     * output (typically a PHP warning or an HTML page served with 200) reaches
     * the log. JsonProcessingException must be caught here: it extends
     * IOException, so letting it escape would land in the callers' generic
     * "IO error during request" wrapper - which is exactly how the body used
     * to get lost.
     */
    private <T> T parseSuccessResponse(Class<?> responseClazz, boolean isList, InputStream inputStream,
            int statusCode, String reasonPhrase) throws IOException {
        HeadRecordingInputStream recordingStream = new HeadRecordingInputStream(inputStream, RESPONSE_HEAD_CAPTURE_BYTES);
        try {
            return parseResponse(responseClazz, isList, recordingStream);
        } catch (JsonProcessingException e) {
            throw new HttpRequestException(statusCode, reasonPhrase, recordingStream.recordedHead(),
                    null, "2xx response body is not the expected JSON: " + e.getOriginalMessage());
        }
    }

    /** Passes bytes through untouched while keeping the first {@code limit} bytes for error reporting. */
    private static final class HeadRecordingInputStream extends FilterInputStream {
        private final ByteArrayOutputStream head;
        private final int limit;

        HeadRecordingInputStream(InputStream in, int limit) {
            super(in);
            this.limit = limit;
            this.head = new ByteArrayOutputStream(Math.min(limit, 512));
        }

        @Override
        public int read() throws IOException {
            int b = super.read();
            if (b != -1 && head.size() < limit) head.write(b);
            return b;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int n = super.read(b, off, len);
            if (n > 0 && head.size() < limit) head.write(b, off, Math.min(n, limit - head.size()));
            return n;
        }

        String recordedHead() throws IOException {
            String recorded = head.toString("UTF-8");
            return head.size() >= limit ? recorded + "... [truncated]" : recorded;
        }
    }

    @SuppressWarnings("unchecked")
	private <T> T parseResponse(Class<?> responseClazz, boolean isList, InputStream inputStream)
			throws IOException {
		if (isList){
			JavaType type = mapper.getTypeFactory().constructParametricType(List.class, responseClazz);
			return mapper.readValue(inputStream, type);
		}
		else return mapper.readValue(inputStream, (Class<T>) responseClazz);
	}

	private URI buildURI(String url){
		return buildURI(url, null);
	}

	private URI buildURI(String url, Map<String, String> params) {
		List<NameValuePair> postParameters = getParametersAsList(params);
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			// 1.0.6: credentials go in the Authorization header by default
			// (see doHttpRequest); query params only in legacy mode.
			if (config.isUseQueryStringAuth()) {
				addCredentialsParams(uriBuilder);
			}
			uriBuilder.addParameters(postParameters);
			return uriBuilder.build();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private void addCredentialsParams(URIBuilder uriBuilder) {
		uriBuilder.addParameter(AuthParamsKey.CONSUMER_KEY.getValue(), this.config.getConsumerKey());
		uriBuilder.addParameter(AuthParamsKey.CONSUMER_SECRET.getValue(), this.config.getConsumerSecret());
	}

	private List<NameValuePair> getParametersAsList(Map<String, String> params) {
		List<NameValuePair> postParameters = new ArrayList<>();
		if (params != null && !params.isEmpty())
			for (String key : params.keySet())
				postParameters.add(new BasicNameValuePair(key, params.get(key)));
		return postParameters;
	}

	private HttpEntity convertObjectToJsonHttpEntity(Object objectToJson) {
		HttpEntity entity;
		try {
			entity = new ByteArrayEntity(this.mapper.writeValueAsBytes(objectToJson), ContentType.APPLICATION_JSON);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed convert objec to json http entity. object: " + objectToJson.toString(), e);
		}
		return entity;
	}

    private String inputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
