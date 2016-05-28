package org.literacyapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.literacyapp.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, DataBaseActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        Log.d(getClass().getName(), "onCreate");
        super.onStart();

        new LoadContentAsyncTask().execute();
    }

    private class LoadContentAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(getClass().getName(), "doInBackground");

            try {
                // TODO: download updated content from server
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(getClass().getName(), null, e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(getClass().getName(), "onPostExecute");
            super.onPostExecute(aVoid);

            //Intent intent = new Intent(getApplicationContext(), ParallaxActivity.class);
            //startActivity(intent);

            finish();
        }
    }
}
