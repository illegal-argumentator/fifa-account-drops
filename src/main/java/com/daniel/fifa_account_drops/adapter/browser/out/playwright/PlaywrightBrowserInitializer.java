package com.daniel.fifa_account_drops.adapter.browser.out.playwright;

import com.daniel.fifa_account_drops.adapter.browser.out.Browser;
import com.daniel.fifa_account_drops.adapter.browser.out.BrowserCdpInitializer;
import com.daniel.fifa_account_drops.adapter.browser.out.BrowserInitializer;
import com.daniel.fifa_account_drops.adapter.browser.out.nst.props.NstProps;
import com.microsoft.playwright.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
@Component
@RequiredArgsConstructor
final class PlaywrightBrowserInitializer implements BrowserInitializer, BrowserCdpInitializer {

    @Value("${browser.headless}")
    private boolean HEADLESS;
    private final NstProps props;

    private final static String NST_BROWSER_URL_TEMPLATE = "ws://%s:%s/devtool/launch/%s?x-api-key=%s&config=%%7B%%22headless%%22%%3A%s%%2C%%22autoClose%%22%%3A%s%%7D";

    @Override
    public Browser init() {
        Playwright playwright = Playwright.create();
        com.microsoft.playwright.Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS));
        Page page = browser.newPage();

        return new PlaywrightBrowser(page, List.of(page, browser, playwright));
    }

    @Override
    public Browser init(String profileId) {
        try {
            String url = String.format(
                    NST_BROWSER_URL_TEMPLATE,
                    props.getHost(),
                    props.getPort(),
                    profileId,
                    props.getApiKey(),
                    HEADLESS,
                    FALSE
            );

            return withCdp(url);
        } catch (PlaywrightException e) {
            log.error("Error connecting to Nst Browser: {}", e.getMessage());
            throw new RuntimeException("Error connecting to Nst Browser. Possibly daily quote reached");
        }
    }

    private Browser withCdp(String cdpUrl) throws PlaywrightException {
        Playwright playwright = Playwright.create();
        com.microsoft.playwright.Browser browser = playwright.chromium().connectOverCDP(cdpUrl);
        BrowserContext context = browser.contexts().get(0);

        Page page = context.pages().isEmpty()
                ? context.newPage()
                : context.pages().get(0);

        page.bringToFront();
        return new PlaywrightBrowser(page, List.of(page, context, browser, playwright));
    }
}
