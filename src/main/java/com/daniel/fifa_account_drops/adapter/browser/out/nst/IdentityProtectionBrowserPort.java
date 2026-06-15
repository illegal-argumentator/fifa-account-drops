package com.daniel.fifa_account_drops.adapter.browser.out.nst;

import com.daniel.fifa_account_drops.adapter.browser.out.nst.dto.CreateProfileResponse;

public interface IdentityProtectionBrowserPort {

    CreateProfileResponse createProfile(String profileName, String proxyUrl);

}
