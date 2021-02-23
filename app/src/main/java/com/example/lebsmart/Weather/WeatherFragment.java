package com.example.lebsmart.Weather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class WeatherFragment extends Fragment {

    ViewGroup viewGroup;

    TextView cityNameTV, descriptionTV, temperatureTV, tempMinMaxTV, sunriseContentTV
            , sunsetContentTV, humidityContentTV, windContentTV;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.weather_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());

        cityNameTV = viewGroup.findViewById(R.id.cityNameTV);
        descriptionTV = viewGroup.findViewById(R.id.descriptionTV);
        temperatureTV = viewGroup.findViewById(R.id.temperatureTV);
        tempMinMaxTV = viewGroup.findViewById(R.id.tempMinMaxTV);
        sunriseContentTV = viewGroup.findViewById(R.id.sunriseContentTV);
        sunsetContentTV = viewGroup.findViewById(R.id.sunsetContentTV);
        humidityContentTV = viewGroup.findViewById(R.id.humidityContentTV);
        windContentTV = viewGroup.findViewById(R.id.windContentTV);

        getWeather();

        return viewGroup;
    }


    public void getWeather() {
        CommonMethods.displayLoadingScreen(progressDialog);

        String content;
        Weather weather = new Weather();

        // set lat and lon of the user's building
        try {
            //content = weather.execute("http://api.openweathermap.org/data/2.5/weather?lat=33.89&lon=35.5&appid=87fb0e0d645394e7b44118d14b0b47f7").get(); // by lat lon
            //content = weather.execute("http://api.openweathermap.org/data/2.5/weather?q=paris&appid=87fb0e0d645394e7b44118d14b0b47f7").get(); // get info by city name
            // temperature units in celsius
            content = weather.execute("http://api.openweathermap.org/data/2.5/weather?lat=34.25&lon=35.66&appid=87fb0e0d645394e7b44118d14b0b47f7&units=metric").get();
            Log.i("content", content);

            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            Log.i("weatherData", weatherData);

            //weatherData is an array
            JSONArray jsonArray = new JSONArray(weatherData);
            String main = "", description = ""; // elements inside the array weather

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject weatherPart = jsonArray.getJSONObject(i);
                main = weatherPart.getString("main"); // main inside array
                description = weatherPart.getString("description");// 1
            }

            Log.i("main", main);
            Log.i("description", description);

            // by me
            String mainObject = "", windObject = "", sysObject = "";
            String temp = "", feelsLike = "", tempMin = "", tempMax = "", humidity = "";
            String windSpeed = "";
            String sunrise = "", sunset = "";
            String name = "", timezone = ""; // city name


            mainObject = jsonObject.getString("main");

            JSONObject jsonObject1 = new JSONObject(mainObject);
            temp = jsonObject1.getString("temp");
            feelsLike = jsonObject1.getString("feels_like");
            tempMin = jsonObject1.getString("temp_min");
            tempMax = jsonObject1.getString("temp_max");
            humidity = jsonObject1.getString("humidity");

            windObject = jsonObject.getString("wind");

            JSONObject jsonObject2 = new JSONObject(windObject);
            windSpeed = jsonObject2.getString("speed");

            sysObject = jsonObject.getString("sys");

            JSONObject jsonObject3 = new JSONObject(sysObject);
            sunrise = jsonObject3.getString("sunrise");
            sunset = jsonObject3.getString("sunset");

            name = jsonObject.getString("name");
            timezone = jsonObject.getString("timezone");

            Log.i("temp", temp + " \u00B0C"); // deg C, changes in api
            Log.i("feelsLike", feelsLike + " \u00B0C"); // deg C
            Log.i("tempMin", tempMin + " \u00B0C"); // deg C
            Log.i("tempMax", tempMax + " \u00B0C"); // deg C
            Log.i("humidity", humidity + " %"); // %, by default

            Log.i("windSpeed", (Math.round(Float.parseFloat(windSpeed) * 3.6)) + " km/h");// m/s by default, change to km/h

            // https://www.w3resource.com/java-exercises/datetime/java-datetime-exercise-36.php
            //Unix seconds
            long sunriseSeconds = Long.parseLong(sunrise);
            long sunsetSeconds = Long.parseLong(sunset);
            //convert seconds to milliseconds
            Date sunriseDate = new Date(sunriseSeconds*1000L);
            Date sunsetDate = new Date(sunsetSeconds*1000L);
            // format of the date
            SimpleDateFormat jdf = new SimpleDateFormat("HH:mm");
            jdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
            String sunriseTime = jdf.format(sunriseDate);
            String sunsetTime = jdf.format(sunsetDate);

            Log.i("sunrise", sunriseTime);
            Log.i("sunset", sunsetTime);

            Log.i("name", name);



            cityNameTV.setText(name);
            descriptionTV.setText(capitalizeString(description));
            temperatureTV.setText(Math.round(Float.parseFloat(temp)) + "\u00B0");
            tempMinMaxTV.setText("H:" + Math.round(Float.parseFloat(tempMax)) + "\u00B0 " + "L:" + Math.round(Float.parseFloat(tempMin)) + "\u00B0");
            sunriseContentTV.setText(sunriseTime);
            sunsetContentTV.setText(sunsetTime);
            humidityContentTV.setText(Math.round(Float.parseFloat(humidity)) + "%");
            windContentTV.setText((Math.round(Float.parseFloat(windSpeed) * 3.6)) + " km/h");

            CommonMethods.dismissLoadingScreen(progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.dismissLoadingScreen(progressDialog);
        }

    }

    public static String capitalizeString(String str){
        String[] words =str.split("\\s");
        String capitalizeWord="";
        for(String w : words){
            String first = w.substring(0,1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }


}
