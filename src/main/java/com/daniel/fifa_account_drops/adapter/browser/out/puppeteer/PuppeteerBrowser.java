package com.daniel.fifa_account_drops.adapter.browser.out.puppeteer;

import com.daniel.fifa_account_drops.adapter.browser.out.Browser;
import com.ruiyun.jvppeteer.api.core.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
final class PuppeteerBrowser implements Browser {

    private final Page page;
    private final List<AutoCloseable> autoCloseables;

    @Override
    public void navigate(String url) {
        try {
            page.goTo(url);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fill(String selector, String value) {

    }

    @Override
    public void waitForSelector(String selector, double timeout, Runnable action) {

    }

    @Override
    public void click(String selector) {

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
