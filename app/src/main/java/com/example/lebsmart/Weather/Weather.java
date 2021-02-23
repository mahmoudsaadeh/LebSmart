package com.example.lebsmart.Weather;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Weather extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... address) {

        try {
            // establish connection with address
            URL url = new URL(address[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // retrieve data from url
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            // retrieve data and return it as string
            int data = inputStreamReader.read();
            String content = "";
            char ch;
            while (data != -1) {
                ch = (char) data;
                content = content + ch;
                data = inputStreamReader.read();
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

}
