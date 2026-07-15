package com.claro.amx.cufjava.change_domestic_use_api.client;

import org.springframework.http.HttpHeaders;

import java.util.Set;

public class HttpHeadersSanitizer {

    private HttpHeadersSanitizer() {
        // Utility class, prevent instantiation
    }

    private static final Set<String> HEADERS_TO_SKIP = Set.of(
            "content-length", "host", "connection"
    );

    public static void copySanitizedHeaders(HttpHeaders source, HttpHeaders target) {
        source.forEach((key, value) -> {
            if (!HEADERS_TO_SKIP.contains(key.toLowerCase())) {
                target.put(key, value);
            }
        });
    }
}
