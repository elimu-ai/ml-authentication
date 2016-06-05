package org.literacyapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class JsonLoader {

    public static String loadJson(String urlValue) {
        Log.d(JsonLoader.class, "loadJson");

        Log.d(JsonLoader.class, "Downloading from " + urlValue + "...");

        String jsonResponse = null;

        try {
            URL url = new URL(urlValue);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            Log.d(JsonLoader.class, "responseCode: " + responseCode);
            InputStream inputStream = null;
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            jsonResponse = bufferedReader.readLine();
        } catch (MalformedURLException e) {
            Log.e(JsonLoader.class, "MalformedURLException", e);
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.e(JsonLoader.class, "ProtocolException", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(JsonLoader.class, "IOException", e);
            e.printStackTrace();
        }

        return jsonResponse;
    }
}
