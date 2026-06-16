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

    private final Page page;
    private final List<AutoCloseable> autoCloseables;
    @Value("${browser.timeout.default}")
    private long DEFAULT_TIMEOUT;

    @Override
    public void navigate(String url) {
        try {
            page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED).setTimeout(DEFAULT_TIMEOUT));
        } catch (PlaywrightException e) {
            log.error("Unable to navigate: {}.", url);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void waitForSelector(String selector) {
        waitForSelector(selector, DEFAULT_TIMEOUT);
    }

    @Override
    public void waitForSelector(String selector, long timeout) {
        try {
            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setTimeout(timeout));
            locator.isVisible(new Locator.IsVisibleOptions().setTimeout(timeout));
        } catch (PlaywrightException e) {
            log.warn("Element %s not found.".formatted(selector));
            throw new RuntimeException(e);
        }
    }


    @Override
    public void fill(String selector, String value) {
        try {
            page.fill(selector, value);
        } catch (PlaywrightException e) {
            log.warn("Element %s not found.".formatted(selector));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void click(String selector) {
        try {
            page.click(selector);
        } catch (PlaywrightException e) {
            log.warn("Element %s not found.".formatted(selector));
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isVisible(String selector) {
        try {
            return page.locator(selector).isVisible();
        } catch (PlaywrightException e) {
            log.warn("Element %s not found.".formatted(selector));
            return false;
        }
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
