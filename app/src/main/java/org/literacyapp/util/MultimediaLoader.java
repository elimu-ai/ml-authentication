package org.literacyapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MultimediaLoader {

    public static String loadJson(String urlValue) {
        Log.d(MultimediaLoader.class, "loadJson");

        Log.d(MultimediaLoader.class, "Downloading from " + urlValue + "...");

        String jsonResponse = null;

        try {
            URL url = new URL(urlValue);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            Log.d(MultimediaLoader.class, "responseCode: " + responseCode);
            InputStream inputStream = null;
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (jsonResponse == null) {
                    jsonResponse = "";
                }
                jsonResponse += line;
            }
        } catch (MalformedURLException e) {
            Log.e(MultimediaLoader.class, "MalformedURLException", e);
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.e(MultimediaLoader.class, "ProtocolException", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(MultimediaLoader.class, "IOException", e);
            e.printStackTrace();
        }

        return jsonResponse;
    }
}
