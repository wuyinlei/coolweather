package com.example.yinlei.coolweather.util;

/**
 * Created by yinlei on 2015/9/10.
 */
public interface HttpCallbackListrner {
    void onFinish(String response);
    void onError(Exception e);
}
