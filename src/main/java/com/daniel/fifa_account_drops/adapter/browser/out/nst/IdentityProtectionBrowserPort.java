package com.daniel.fifa_account_drops.adapter.browser.out.nst;

import com.daniel.fifa_account_drops.adapter.browser.out.nst.dto.ProfileResponse;

public interface IdentityProtectionBrowserPort {

    ProfileResponse createProfile(String profileName, String proxyUrl);

}
