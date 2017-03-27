package org.literacyapp.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RootHelper {
    public static String runAsRoot(String[] commands) throws IOException, InterruptedException {
        Log.i(RootHelper.class.getName(), "runAsRoot");

        Process process = Runtime.getRuntime().exec("su");

        DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
        for (String command : commands) {
            Log.i(RootHelper.class.getName(), "command: " + command);
            dataOutputStream.writeBytes(command + "\n");
        }
        dataOutputStream.writeBytes("exit\n");
        dataOutputStream.flush();

        process.waitFor();
        int exitValue = process.exitValue();
        Log.i(RootHelper.class.getName(), "exitValue: " + exitValue);

        InputStream inputStreamSuccess = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamSuccess));
        String successMessage = bufferedReader.readLine();
        Log.i(RootHelper.class.getName(), "successMessage: " + successMessage);

        InputStream inputStreamError = process.getErrorStream();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStreamError));
        String errorMessage = bufferedReader.readLine();
        if (TextUtils.isEmpty(errorMessage)) {
            Log.i(RootHelper.class.getName(), "errorMessage: " + errorMessage);
        } else {
            Log.e(RootHelper.class.getName(), "errorMessage: " + errorMessage);
        }
        return successMessage;
    }

    public static boolean isDeviceRooted() {
        Log.i(RootHelper.class.getName(), "isDeviceRooted");

        Process process = null;
        String line = "";

        try {
            process = Runtime.getRuntime().exec("su");
            InputStream inputStreamSuccess = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamSuccess));
            line = bufferedReader.readLine();
        } catch (IOException e) {
            Log.e(RootHelper.class.getName(), null, e);
            line = null;
        }

        return (line != null);
    }
}
