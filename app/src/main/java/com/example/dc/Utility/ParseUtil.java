package com.example.dc.Utility;

import com.example.dc.Weather.db.City;
import com.example.dc.Weather.db.County;
import com.example.dc.Weather.db.Province;
import com.example.dc.Weather.gson.Weather;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParseUtil {

    public static boolean handleProvinceResponse(String response_text){
                try {
                    JSONArray jsonArray = new JSONArray(response_text);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Province province = new Province();
                        province.setProvinceCode(jsonObject.getInt("id"));
                        province.setProvinceName(jsonObject.getString("name"));
                        province.save();
                    }
                    return true;
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return false;
    }

    public static boolean handleCityResponse(String response_text,int provinceCode){
                    try {
                        JSONArray jsonArray = new JSONArray(response_text);
                        for (int i=0; i<jsonArray.length();i++){
                            City city = new City();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            city.setCityCode(jsonObject.getInt("id"));
                            city.setCityName(jsonObject.getString("name"));
                            city.setProvinceId(provinceCode);
                            city.save();
                        }
                        return true;
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                return false;
    }


    public static boolean handleCountyResponse(String response_text, final int ciytId){
                    try {
                        JSONArray jsonArray = new JSONArray(response_text);
                        for (int i=0;i<jsonArray.length();i++){
                            County county = new County();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            county.setCountyName(jsonObject.getString("name"));
                            county.setCountyId(jsonObject.getInt("id"));
                            county.setWeatherId(jsonObject.getString("weather_id"));
                            county.setCityId(ciytId);
                            county.save();
                        }
                        return true;
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    return false;
    }

    public static Weather handleWeatherResponse(String response_text){
                    try {
                        JSONObject jsonObject = new JSONObject(response_text);
                        JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
                        String weatherContent = jsonArray.getJSONObject(0).toString();
                        Gson gson = new Gson();
                        return gson.fromJson(weatherContent,Weather.class);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    return  null;
    }
}
