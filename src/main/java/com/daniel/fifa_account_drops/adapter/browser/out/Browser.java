package com.daniel.fifa_account_drops.adapter.browser.out;

public interface Browser extends AutoCloseable {

    void navigate(String url);

    void fill(String selector, String value);

    void waitForSelector(String selector, double timeout, Runnable action);

    void click(String selector);

}
