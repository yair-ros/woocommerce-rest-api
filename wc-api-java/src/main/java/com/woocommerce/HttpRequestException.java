package com.woocommerce;

import lombok.Getter;

@Getter
public class HttpRequestException extends RuntimeException {
    private final int statusCode;
    private final String reasonPhrase;
    private final String responseBody;

    private final String errorCode;
    private final String errorMessage;

    public HttpRequestException(int statusCode, String reasonPhrase, String responseBody, String errorCode, String errorMessage) {
        super(buildMessage(statusCode, reasonPhrase, errorCode, errorMessage, responseBody));
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.responseBody = responseBody;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    private static String buildMessage(int statusCode, String reasonPhrase, String errorCode, String errorMessage, String responseBody) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP ").append(statusCode).append(" - ").append(reasonPhrase);
        if (errorCode != null) {
            sb.append("\nError Code: ").append(errorCode);
        }
        if (errorMessage != null) {
            sb.append("\nError Message: ").append(errorMessage);
        }
        if (responseBody != null && !responseBody.isEmpty()) {
            sb.append("\nRaw Response: ").append(sanitizeResponse(responseBody));
        }
        return sb.toString();
    }

    private static String sanitizeResponse(String responseBody) {
        if (responseBody == null) return null;

        // Mask consumer_key and consumer_secret values in query strings or JSON
        return responseBody
                .replaceAll("(?i)(consumer_key=)([^&\\s\"]+)", "$1***REDACTED***")
                .replaceAll("(?i)(consumer_secret=)([^&\\s\"]+)", "$1***REDACTED***");
    }

}
