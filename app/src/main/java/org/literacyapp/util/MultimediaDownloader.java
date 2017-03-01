package org.literacyapp.util;

import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MultimediaDownloader {

    public static byte[] downloadMultimedia(String urlValue) {
        Log.i(MultimediaDownloader.class.getName(), "downloadMultimedia");

        Log.i(MultimediaDownloader.class.getName(), "Downloading from " + urlValue + "...");

        byte[] bytes = null;

        try {
            URL url = new URL(urlValue);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            Log.i(MultimediaDownloader.class.getName(), "responseCode: " + responseCode);
            InputStream inputStream = null;
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
                bytes = IOUtils.toByteArray(inputStream);
            } else {
                inputStream = httpURLConnection.getErrorStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String response = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                Log.w(MultimediaDownloader.class.getName(), "responseCode: " + responseCode + ", response: " + response);
            }
        } catch (MalformedURLException e) {
            Log.e(MultimediaDownloader.class.getName(), "MalformedURLException", e);
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.e(MultimediaDownloader.class.getName(), "ProtocolException", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(MultimediaDownloader.class.getName(), "IOException", e);
            e.printStackTrace();
        }

        return bytes;
    }
}
