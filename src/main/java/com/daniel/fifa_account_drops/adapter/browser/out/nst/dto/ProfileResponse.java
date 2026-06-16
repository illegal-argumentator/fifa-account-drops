package com.daniel.fifa_account_drops.adapter.browser.out.nst.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class ProfileResponse {

    private int code;
    private String msg;
    private boolean err;
    private CreateProfileResponseData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateProfileResponseData {

        private String profileId;
        private String groupId;
    }

}
