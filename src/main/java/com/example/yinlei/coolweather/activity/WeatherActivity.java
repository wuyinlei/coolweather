package com.example.yinlei.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yinlei.coolweather.R;
import com.example.yinlei.coolweather.util.HttpCallbackListrner;
import com.example.yinlei.coolweather.util.HttpUtil;
import com.example.yinlei.coolweather.util.Utility;

public class WeatherActivity extends Activity implements View.OnClickListener {

    private LinearLayout weatherInfoLayout;

    /**
     * 用来显示城市名
     */
    private TextView cityNameText;

    /**
     * 用来显示发布时间
     */
    private TextView publishText;

    /**
     * 用来显示天气描述信息
     */
    private TextView weatherDespText;

    /**
     * 用来显示气温1
     */
    private TextView temp1Text;

    /**
     * 用来显示气温2
     */
    private TextView temp2Text;

    /**
     * 用来显示当前日期
     */
    private TextView currentDataText;

    //切换城市
    private Button switchCity;

    //更新天气
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);

        //初始化各种控件
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDataText = (TextView) findViewById(R.id.current_data);
        String countryCode = getIntent().getStringExtra("country_code");
        switchCity = (Button) findViewById(R.id.switch_city);
        refreshWeather = (Button) findViewById(R.id.refresh_weather);
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        if(!TextUtils.isEmpty(countryCode)){
            //有县级代号就去查询天气
            publishText.setText("同步中....");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countryCode);
        } else {
            //没有县级代号的就直接显示本地天气
            showWeather();
        }
    }

    /**
     *
     * @param countryCode   查询县级别代号所对应的天气代号
     */
    private void queryWeatherCode(String countryCode){
        String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
        queryFromServer(address,"countryCode");
    }

    /**
     *
     * @param weatherCode   查询天气代号所对应的天气
     */
    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(address,"weatherCode");
    }

    /**
     *
     * @param address   根据传入的地址和类型去向服务器查询天气代号或者天气信息
     * @param type
     */
    private void queryFromServer(final String address,final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListrner() {
            @Override
            public void onFinish(final String response) {
                if("countryCode".equals(type)){
                    if(!TextUtils.isEmpty(response)){
                        //从服务器返回的数据中解析天气代号
                        String[] array = response.split("\\|");
                        if(array != null && array.length == 2){
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)){
                    //处理服务器返回的天气信息
                    Utility.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       publishText.setText("同步失败");
                   }
               });
            }
        });
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上
     */
    private void showWeather(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(pref.getString("city_name",""));
        temp1Text.setText(pref.getString("temp1",""));
        temp2Text.setText(pref.getString("temp2", ""));
        weatherDespText.setText(pref.getString("weather_desp", ""));
        publishText.setText("今天" + pref.getString("publish_time","") + "发布");
        currentDataText.setText(pref.getString("current_data", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_city:
                Intent intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                publishText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("weather_code","");
                if (!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
                break;
            default:
                break;
        }
    }
}
