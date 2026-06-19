package com.daniel.fifa_account_drops.adapter.http.out.okhttp;

import com.daniel.fifa_account_drops.adapter.http.out.HttpBody;
import com.daniel.fifa_account_drops.adapter.http.out.HttpService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
@RequiredArgsConstructor
final class OkHttpService implements HttpService {

    private final ObjectMapper objectMapper;

    private final OkHttpClient okHttpClient;

    @Override
    public <T> T execute(HttpBody httpBody, Class<T> responseTarget) {
        return objectMapper.readValue(execute(httpBody), responseTarget);
    }

    @Override
    public String execute(HttpBody httpBody) {
        Request.Builder request = new Request.Builder().url(httpBody.url()).headers(Headers.of(httpBody.headers()));

        if (httpBody.body() != null) {
            okhttp3.MediaType mediaType = okhttp3.MediaType.get(MediaType.APPLICATION_JSON_VALUE);
            request.method(httpBody.method().name(), RequestBody.create(objectMapper.writeValueAsString(httpBody.body()), mediaType));
        }

        try (Response response = okHttpClient.newCall(request.build()).execute()) {

            ResponseBody body = response.body();
            String responseBody = body != null ? body.string() : "";

            if (!response.isSuccessful()) {
                throw new RuntimeException(
                        "API error. Code: " + response.code() + ", Body: " + responseBody
                );
            }

            return responseBody;

        } catch (IOException e) {
            throw new RuntimeException("Failed to execute API request", e);
        }
    }
}
