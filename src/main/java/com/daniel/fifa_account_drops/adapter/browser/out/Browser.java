package com.daniel.fifa_account_drops.adapter.browser.out;

import java.util.concurrent.Callable;

public interface Browser extends AutoCloseable {

    void navigate(String url);
    void navigate(String url, String selector, Callable<? extends RuntimeException> cal) throws Exception;

    boolean isVisible(String selector, long timeout);

    void waitForSelector(String selector);
    void waitForSelector(String selector, long timeout);
    void waitForSelector(String selector, String selector2, long timeout, Callable<? extends RuntimeException> cal) throws Exception;

    void fill(String selector, String value);
    void fillHumanly(String selector, String value);

    void click(String selector);

    boolean isVisible(String selector);

    byte[] screenshot(String selector);
}
