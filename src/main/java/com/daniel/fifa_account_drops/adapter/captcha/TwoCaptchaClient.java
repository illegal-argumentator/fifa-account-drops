package com.daniel.fifa_account_drops.adapter.captcha;

import com.daniel.fifa_account_drops.adapter.http.out.HttpBody;
import com.daniel.fifa_account_drops.adapter.http.out.HttpService;
import com.daniel.fifa_account_drops.shared.WaitUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TwoCaptchaClient implements CaptchaClient {

    @Value("${captcha.key}")
    private String API_KEY;

    private static final String BASE_URL = "https://api.2captcha.com";
    private static final String CREATE_TASK = "/createTask";
    private static final String GET_TASK_RESULT = "/getTaskResult";
    private static final String CLIENT_KEY_PREFIX = "clientKey";
    private static final String TASK_ID_PREFIX = "taskId";

    private final HttpService httpService;

    private final ObjectMapper OBJECT_MAPPER;

    @Override
    public String handleNumbers(byte[] bytes) {
        long taskId = createTask(bytes);
        return waitForResult(taskId);
    }

    private long createTask(byte[] bytes) {
        Map<String, Object> task = Map.of("type", "ImageToTextTask", "body", new String(Base64.getDecoder().decode(bytes)));
        Map<String, Object> request = Map.of(CLIENT_KEY_PREFIX, API_KEY, "task", task);
        String json = OBJECT_MAPPER.writeValueAsString(request);

        HttpBody httpBody = new HttpBody(BASE_URL + CREATE_TASK, HttpBody.Method.POST, json, Map.of());
        String response = httpService.execute(httpBody);
        JsonNode node = OBJECT_MAPPER.readTree(response);

        if (node.get("errorId").asInt() != 0) {
            throw new RuntimeException("2Captcha error: " + response);
        }

        return node.get(TASK_ID_PREFIX).asLong();
    }

    private String waitForResult(long taskId) {
        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = Map.of(CLIENT_KEY_PREFIX, API_KEY, TASK_ID_PREFIX, taskId);
            String json = OBJECT_MAPPER.writeValueAsString(map);

            HttpBody httpBody = new HttpBody(BASE_URL + GET_TASK_RESULT, HttpBody.Method.POST, json, Map.of());
            String response = httpService.execute(httpBody);
            JsonNode node = OBJECT_MAPPER.readTree(response);

            if ("ready".equals(node.get("status").asText())) {
                return node.get("solution").get("text").asText();
            }

            WaitUtils.waitSafely(3);
        }

        throw new RuntimeException("Captcha solving timeout.");
    }
}
