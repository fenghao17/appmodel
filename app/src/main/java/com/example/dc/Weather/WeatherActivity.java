package com.example.dc.Weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dc.R;
import com.example.dc.Utility.ParseUtil;
import com.example.dc.Utility.Util;
import com.example.dc.Weather.db.City;
import com.example.dc.Weather.db.County;
import com.example.dc.Weather.db.Province;
import com.example.dc.Weather.gson.Forecast;
import com.example.dc.Weather.gson.Weather;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ListView area_weather;
    private WeatherAdapter adapter;
    private List<Province> provinceList = new ArrayList<>();
    private List<City> cityList = new ArrayList<>();
    private List<County> countyList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private int current_level = 0;
    private final int PROVINCE = 0;
    private final int CITY = 1;
    private final int COUNTY = 2;
    private int provinceCode;
    private int cityCode;
    private String weatherId;
    private DrawerLayout weather_drawer;
    private ScrollView weather_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        area_weather = findViewById(R.id.area_weather);
        adapter = new WeatherAdapter(this, R.layout.weather_layout, nameList);
        area_weather.setAdapter(adapter);
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        weather_drawer = findViewById(R.id.weather_drawer);
        weather_layout = findViewById(R.id.weather_layout);

        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.isEmpty()) {
            queryFromService("http://guolin.tech/api/china", "province");
        } else {
            init();
        }

        area_weather.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String name = nameList.get(position);
                //请求city信息
                if (current_level == PROVINCE) {
                    for (int i = 0; i < provinceList.size(); i++) {
                        while (name.equals(provinceList.get(i).getProvinceName())) {
                            provinceCode = provinceList.get(i).getProvinceCode();
                            break;
                        }
                    }
                    current_level = CITY;
                    DataSupport.deleteAll(City.class);
                    String url = "http://guolin.tech/api/china/" + provinceCode;
                    queryFromService(url,"city");
                    //请求county信息
                } else if (current_level == CITY) {
                    for (int i=0;i<cityList.size();i++){
                        while (name.equals(cityList.get(i).getCityName())){
                            cityCode = cityList.get(i).getCityCode();
                            break;
                        }
                    }
                    current_level = COUNTY;
                        String url = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
                        queryFromService(url,"county");

                } else if (current_level == COUNTY) {
                        for(int i=0;i<countyList.size();i++){
                            while (name.equals(countyList.get(i).getCountyName())){
                                weatherId = countyList.get(i).getWeatherId();
                                break;
                            }
                        }
                        current_level = PROVINCE;
                        weather_drawer.closeDrawer(GravityCompat.START);
                        requestWeather(weatherId);
                }
            }
        });

    }

    public void requestWeather(final String weatherId){
        String url = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=2b1f5b4912994e55a27ff785a99edcf3";
        Util.sendRequsetWithOkhttp(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            final String responseText = response.body().string();
            final Weather weather = ParseUtil.handleWeatherResponse(responseText);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (weather != null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                        showWeatherInfo(weather);
                    }
                }
            });
            }
        });
    }


        public void init () {
            nameList.clear();
            if (current_level == PROVINCE){
                for (int i = 0; i < provinceList.size(); i++) {
                    nameList.add(provinceList.get(i).getProvinceName());
                }
            }else if(current_level == CITY){
                for (int i=0; i<cityList.size();i++){
                    nameList.add(cityList.get(i).getCityName());
                }
            }else if (current_level == COUNTY){
                for (int i=0;i<countyList.size();i++){
                    nameList.add(countyList.get(i).getCountyName());
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }

        public void queryFromService(String url,String level){
            if(level.equals("province")){
                Util.sendRequsetWithOkhttp(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String response_text = response.body().string();
                        boolean result = ParseUtil.handleProvinceResponse(response_text);
                        if (result){
                            init();
                        }
                    }
                });
            }else if(level.equals("city")){
                DataSupport.deleteAll(City.class);
                Util.sendRequsetWithOkhttp(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String response_text = response.body().string();
                        boolean result = ParseUtil.handleCityResponse(response_text,provinceCode);
                        if (result){
                            cityList = DataSupport.findAll(City.class);
                            init();
                        }
                    }
                });
            }
            else if(level.equals("county")){
                DataSupport.deleteAll(County.class);
                Util.sendRequsetWithOkhttp(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String response_text = response.body().string();
                        boolean result = ParseUtil.handleCountyResponse(response_text,cityCode);
                        if (result){
                            countyList = DataSupport.findAll(County.class);
                            init();
                        }
                    }
                });
            }
        }

        public void showWeatherInfo(Weather weather){
            String cityName = weather.basic.cityName;
            String updateTime = weather.basic.update.updateTime.split(" ")[1];
            String degree = weather.now.temperature + "度";
            String weatherInfo = weather.now.more.info;
            if (weatherInfo.equals("多云")||weatherInfo.equals("阴")){
                weather_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.yintian_weather));
            }else if (weatherInfo.equals("小雨")||weatherInfo.equals("中雨")||weatherInfo.equals("大雨")){
                weather_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.yutian_weather));
            }else {
                weather_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.qingtian_weather));
            }
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherInfo);
            forecastLayout.removeAllViews();
            for (Forecast forecast:weather.forecastList){
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
                TextView dateText = view.findViewById(R.id.date_text);
                TextView infoText = view.findViewById(R.id.info_text);
                TextView maxText = view.findViewById(R.id.max_text);
                TextView minText = view.findViewById(R.id.min_text);

                dateText.setText(forecast.date);
                infoText.setText(forecast.more.info);
                maxText.setText(forecast.temperature.max);
                minText.setText(forecast.temperature.min);
                forecastLayout.addView(view);
            }

            if (weather.aqi!=null){
                aqiText.setText(weather.aqi.city.aqi);
                pm25Text.setText(weather.aqi.city.pm25);
            }

            String comfort = "舒适度" + weather.suggestion.comfort.info;
            String carWash = "洗车指数" + weather.suggestion.carWash.info;
            String sport = "运动建议" + weather.suggestion.sport.info;
            comfortText.setText(comfort);
            carWashText.setText(carWash);
            sportText.setText(sport);
            weatherLayout.setVisibility(View.VISIBLE);
        }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        current_level = 0;
    }
}
