package org.literacyapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import org.literacyapp.dao.DaoMaster;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.Number;
import org.literacyapp.dao.NumberDao;
import org.literacyapp.util.Log;

/**
 * Created by root on 27/05/16.
 */
public class DataBaseActivity extends AppCompatActivity {


    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private NumberDao numberDao;
    private Number number;
    private int value = 0;
    private String language;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(getApplicationContext().getApplicationContext(), "mydatabase", null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        numberDao = daoSession.getNumberDao();
        daoMaster.createAllTables(db, true);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        EditText languageText = (EditText) findViewById(R.id.languageText);
        EditText valueText = (EditText) findViewById(R.id.valueText);

        try {
            value = Integer.parseInt(valueText.getText().toString());
            language = languageText.getText().toString();

        }catch (NumberFormatException numberExc) {
            Log.d(numberExc.toString(), "EXCEPCION");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = new Number();

                daoMaster.createAllTables(db, true);

                number.setLanguage(language);
                number.setValue(value);
                numberDao.insertOrReplace(number);

            }
        });


    }

}
