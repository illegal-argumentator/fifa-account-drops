package com.daniel.fifa_account_drops.adapter.account.out;

import com.daniel.fifa_account_drops.adapter.http.out.HttpBody;
import com.daniel.fifa_account_drops.adapter.http.out.HttpService;
import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.port.external.ExternalAccountQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExternalAccountQueryAdapter implements ExternalAccountQueryPort {

    private final HttpService httpService;

    @Value("${app.server.url}")
    private String SERVER_URL;

    @Override
    public Account retrieve() {
        HttpBody httpBody = new HttpBody(SERVER_URL + "/accounts/pick", HttpBody.Method.GET, null, Map.of());
        return httpService.execute(httpBody, Account.class);
    }
}
