package com.woocommerce.auth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BasicAuthConfigTest {

    @Test
    public void threeArgConstructorAppliesDefaultTimeouts() {
        BasicAuthConfig config = new BasicAuthConfig("https://example.com", "ck", "cs");
        assertEquals(BasicAuthConfig.DEFAULT_CONNECT_TIMEOUT_MILLIS, config.getConnectTimeoutMillis());
        assertEquals(BasicAuthConfig.DEFAULT_SOCKET_TIMEOUT_MILLIS, config.getSocketTimeoutMillis());
    }

    @Test
    public void explicitTimeoutsAreKept() {
        BasicAuthConfig config = new BasicAuthConfig("https://example.com", "ck", "cs", 5_000, 30_000);
        assertEquals(5_000, config.getConnectTimeoutMillis());
        assertEquals(30_000, config.getSocketTimeoutMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroConnectTimeoutRejected() {
        new BasicAuthConfig("https://example.com", "ck", "cs", 0, 30_000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeSocketTimeoutRejected() {
        new BasicAuthConfig("https://example.com", "ck", "cs", 5_000, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingCredentialsStillRejected() {
        new BasicAuthConfig("https://example.com", "", "cs");
    }
}
