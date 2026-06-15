package com.daniel.fifa_account_drops.adapter.browser.out.puppeteer;

import com.daniel.fifa_account_drops.adapter.browser.out.Browser;
import com.daniel.fifa_account_drops.adapter.browser.out.BrowserInitializer;
import com.ruiyun.jvppeteer.api.core.Page;
import com.ruiyun.jvppeteer.cdp.core.Puppeteer;
import com.ruiyun.jvppeteer.cdp.entities.LaunchOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

//@Component
@RequiredArgsConstructor
final class PuppeteerBrowserInitializer implements BrowserInitializer {

    @Value("${browser.headless}")
    private boolean HEADLESS;

    @Override
    public Browser init() {
        try {
            com.ruiyun.jvppeteer.api.core.Browser browser = Puppeteer.launch(LaunchOptions.builder().headless(HEADLESS).build());
            Page page = browser.newPage();
            return new PuppeteerBrowser(page, List.of(browser));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Browser initOverCdp(String profileId) {
        return null;
    }
}
