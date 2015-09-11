package com.example.yinlei.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yinlei.coolweather.model.City;
import com.example.yinlei.coolweather.model.Country;
import com.example.yinlei.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinlei on 2015/9/10.
 */
public class CoolWeatherDB {

    /**
     * 数据库名字
     */
    public static final String DB_NAME = "cool_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 2;

    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;


    /**
     *
     * @param context  将构造函数私有化
     */
    private CoolWeatherDB (Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     *
     * @param context
     * @return   获取CoolWeatherDB的实例
     */
    public synchronized static CoolWeatherDB getInstance (Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     *
     * @param province    将Province实例存储到数据库
     */
    public void saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }

    /**
     *
     * @return从数据库中读取全国所有的省份信息
     */
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while(cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return list;
    }

    /**
     *
     * @param city   将City实例存数到数据库
     */
    public void saveCity(City city){
        if(city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());

            //添加数据的时候，insert接受三个参数，第一个是表名，第二个是用于在未指定添加数据的
            //情况下给某些可为空的列自动赋值null，一般用不到这个功能，直接传入null，第三个是ContentValues对象
            db.insert("City", null, values);
        }
    }

    /**
     *
     * @param provinceId
     * @return  从数据库中读取某省下所有的城市信息
     */
    public List<City> loadCity(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return list;
    }


    /**
     *
     * @param country   将Country实例存储到数据库中
     */
    public void savaCounty(Country country){
        if(country != null){
            ContentValues values = new ContentValues();
            values.put("country_name",country.getCountryName());
            values.put("country_code",country.getCountryCode());
            values.put("city_id",country.getCityId());
            db.insert("Country",null,values);
        }
    }

    /**
     *
     * @param cityId
     * @return   从数据库中读取某城市下的所有县的信息
     */
    public List<Country> loadCountry(int cityId){
        List<Country> list = new ArrayList<Country>();
        Cursor cursor = db.query("Country",null,"city_id = ?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityId(cityId);
                list.add(country);
            } while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return list;
    }

}
