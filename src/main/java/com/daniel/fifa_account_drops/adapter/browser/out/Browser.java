package com.daniel.fifa_account_drops.adapter.browser.out;

public interface Browser extends AutoCloseable {

    void navigate(String url);

    void waitForSelector(String selector);

    void waitForSelector(String selector, long timeout);

    void fill(String selector, String value);

    void click(String selector);

    boolean isVisible(String selector);

}
