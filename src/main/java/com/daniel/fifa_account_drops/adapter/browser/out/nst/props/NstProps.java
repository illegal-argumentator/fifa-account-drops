package com.daniel.fifa_account_drops.adapter.browser.out.nst.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "nst")
public class NstProps {

    private String apiKey;
    private String url;
    private String groupId;
    private String host;
    private int port;

}
