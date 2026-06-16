package com.daniel.fifa_account_drops.adapter.http.out;

import java.util.Map;

public record HttpBody(
        String url,
        Method method,
        Object body,
        Map<String, String> headers
) {

    public enum Method {
        POST,
        GET
    }

}
