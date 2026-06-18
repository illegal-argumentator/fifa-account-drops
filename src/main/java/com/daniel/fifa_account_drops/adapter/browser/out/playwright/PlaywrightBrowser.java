package com.daniel.fifa_account_drops.adapter.browser.out.playwright;

import com.daniel.fifa_account_drops.adapter.browser.out.Browser;
import com.microsoft.playwright.Keyboard;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitUntilState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Callable;

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
            page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED).setTimeout(DEFAULT_TIMEOUT * 2));
        } catch (PlaywrightException e) {
            log.error("Unable to navigate: {}.", url);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void navigate(String url, String selector, Callable<? extends RuntimeException> cal) throws Exception {
        try {
            navigate(url);
            Locator locator = page.locator(selector);

            if (locator.isVisible()) {
                throw cal.call();
            }
        } catch (PlaywrightException e) {
            log.warn("Element %s not found.".formatted(selector));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void waitForSelector(String selector) {
        waitForSelector(selector, DEFAULT_TIMEOUT * 2);
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
    public void waitForSelector(String selector, String selector2, long timeout, Callable<? extends RuntimeException> cal) throws Exception {
        try {
            Locator locator = page.locator(selector);
            Locator locator2 = page.locator(selector2);
            Locator combined = locator.or(locator2);

            combined.first().waitFor(new Locator.WaitForOptions().setTimeout(timeout));
            if (locator2.isVisible()) {
                throw cal.call();
            }
        } catch (PlaywrightException e) {
            log.warn("Element %s not found.".formatted(selector));
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isVisible(String selector, long timeout) {
        try {
            Locator locator = page.locator(selector);
            page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeout));
            return locator.isVisible();
        } catch (PlaywrightException e) {
            return false;
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
    public void fillHumanly(String selector, String value) {
        try {
            page.click(selector);
            page.keyboard().type(value, new Keyboard.TypeOptions().setDelay(90));
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
    public byte[] screenshot(String selector) {
        try {
            Locator locator = page.locator(selector);
            return locator.screenshot();
        } catch (PlaywrightException e) {
            log.warn("Element %s not found.".formatted(selector));
            throw new RuntimeException(e);
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
