package com.daniel.fifa_account_drops.adapter.browser.out.nst;

import com.daniel.fifa_account_drops.adapter.browser.out.nst.dto.CreateProfileRequest;
import com.daniel.fifa_account_drops.adapter.browser.out.nst.dto.ProfileResponse;
import com.daniel.fifa_account_drops.adapter.browser.out.nst.props.NstProps;
import com.daniel.fifa_account_drops.adapter.http.out.HttpBody;
import com.daniel.fifa_account_drops.adapter.http.out.HttpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NstClient implements IdentityProtectionBrowserPort {

    private final NstProps props;
    private final HttpService httpService;

    @Override
    public ProfileResponse createProfile(String profileName, String proxyUrl) {
        CreateProfileRequest request = new CreateProfileRequest(profileName, proxyUrl, props.getGroupId());
        HttpBody httpBody = new HttpBody(
                props.getUrl() + "/profiles",
                HttpBody.Method.POST,
                request,
                Map.of("x-api-key", props.getApiKey())
        );

        return httpService.execute(httpBody, ProfileResponse.class);
    }

}
