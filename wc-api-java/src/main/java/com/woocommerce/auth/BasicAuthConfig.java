package com.woocommerce.auth;

public final class BasicAuthConfig {

    /** Max time to establish the TCP/TLS connection. */
    public static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 10_000;
    /** Max time between two data packets once connected (a hung server trips this, not slow-but-flowing pages). */
    public static final int DEFAULT_SOCKET_TIMEOUT_MILLIS = 60_000;

    private final String url;
    private final String consumerKey;
    private final String consumerSecret;
    private final int connectTimeoutMillis;
    private final int socketTimeoutMillis;

    public BasicAuthConfig(String url, String consumerKey, String consumerSecret) {
        this(url, consumerKey, consumerSecret, DEFAULT_CONNECT_TIMEOUT_MILLIS, DEFAULT_SOCKET_TIMEOUT_MILLIS);
    }

    public BasicAuthConfig(String url, String consumerKey, String consumerSecret,
                           int connectTimeoutMillis, int socketTimeoutMillis) {
        if (url == null || url.isEmpty() ||
                consumerKey == null || consumerKey.isEmpty() ||
                consumerSecret == null || consumerSecret.isEmpty()) {
            throw new IllegalArgumentException("All arguments are required");
        }
        if (connectTimeoutMillis <= 0 || socketTimeoutMillis <= 0) {
            throw new IllegalArgumentException("Timeouts must be positive");
        }
        this.url = url;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.socketTimeoutMillis = socketTimeoutMillis;
    }

    public String getUrl() {
        return url;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public int getSocketTimeoutMillis() {
        return socketTimeoutMillis;
    }
}
