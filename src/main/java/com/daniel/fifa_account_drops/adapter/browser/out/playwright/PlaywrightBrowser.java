package com.daniel.fifa_account_drops.adapter.browser.out.playwright;

import com.daniel.fifa_account_drops.adapter.browser.out.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitUntilState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
final class PlaywrightBrowser implements Browser {

    @Value("${browser.timeout.default}")
    private long DEFAULT_TIMEOUT;

    private final Page page;
    private final List<AutoCloseable> autoCloseables;

    @Override
    public void navigate(String url) {
        page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED).setTimeout(DEFAULT_TIMEOUT));
    }


    @Override
    public void waitForSelector(String selector, double timeout, Runnable action) {
        Locator locator = page.locator(selector);
        boolean appeared;

        try {
            locator.waitFor(new Locator.WaitForOptions().setTimeout(timeout));
            appeared = locator.isVisible(new Locator.IsVisibleOptions().setTimeout(timeout));

            if (appeared) {
                action.run();;
            }
        } catch (PlaywrightException e) {
            log.warn("Element %s not found.".formatted(selector));
        }
    }

    @Override
    public void fill(String selector, String value) {
        page.fill(selector, value);
    }

    @Override
    public void click(String selector) {
        page.click(selector);
    }

    @Override
    public void close() {
        if (CollectionUtils.isEmpty(autoCloseables)) {
            return;
        }

        for (AutoCloseable autoCloseable : autoCloseables) {
            try {
                autoCloseable.close();
            } catch (Exception e) {
                log.error("Exception while closing browser: {}.", e.getMessage());
            }
        }
    }
}
