package com.example.yinlei.coolweather.util;

import android.text.TextUtils;

import com.example.yinlei.coolweather.db.CoolWeatherDB;
import com.example.yinlei.coolweather.model.City;
import com.example.yinlei.coolweather.model.Country;
import com.example.yinlei.coolweather.model.Province;

/**
 * Created by yinlei on 2015/9/10.
 */
public class Utility {

    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析出来的数据存储到Province表中
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param coolWeatherDB
     * @param response
     * @param provinceId
     * @return   解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){

        if(!TextUtils.isEmpty(response)){
            String []allCities = response.split(",");
            if(allCities != null&& allCities.length > 0){
                for (String c:allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    //将解析出来的数据存储到city表
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }


    /**
     *
     * @param coolWeatherDB
     * @param response
     * @param cityId
     * @return   解析和处理服务返回的县级数据
     */
    public static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){

        if(!TextUtils.isEmpty(response)){
            String []allCountries = response.split(",");
            if(allCountries != null&& allCountries.length > 0){
                for (String c:allCountries) {
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setCountryCode(array[0]);
                    country.setCountryName(array[1]);
                    country.setCityId(cityId);
                    //将解析出来的数据存储到city表
                    coolWeatherDB.savaCounty(country);
                }
                return true;
            }
        }
        return false;
    }
}
