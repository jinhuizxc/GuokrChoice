package com.example.jh.guokrchoice.support.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public static String get(String urlAddress) throws IOException {

        URL url = new URL(urlAddress);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        try {
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
                return result.toString();
            } else {
                throw new IOException("network error:" + urlConnection.getResponseCode());
            }
        }finally {
            urlConnection.disconnect();
        }
    }

    public static String get(String urlAddress,HttpParams params) throws IOException {
        return get(urlAddress+params.getParams());
    }
}
