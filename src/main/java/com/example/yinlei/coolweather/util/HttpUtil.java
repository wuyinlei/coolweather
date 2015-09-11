package com.example.yinlei.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yinlei on 2015/9/10.
 * 这个是和服务器交互的
 */
public class HttpUtil {

    public static  void sendHttpRequest(final  String address,final HttpCallbackListrner listrner){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    if (listrner != null){
                        //回调onFinish()方法
                        listrner.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listrner != null){
                        listrner.onError(e);
                    }
                } finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
