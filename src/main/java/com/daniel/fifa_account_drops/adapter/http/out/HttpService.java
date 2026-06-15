package com.daniel.fifa_account_drops.adapter.http.out;

public interface HttpService {

    <T> T execute(HttpBody body, Class<T> responseTarget);

    String execute(HttpBody body);

}
