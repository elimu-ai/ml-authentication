package org.literacyapp.content.number;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.literacyapp.R;
import org.literacyapp.dao.DaoMaster;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.Number;
import org.literacyapp.dao.NumberDao;
import org.literacyapp.util.Log;

import java.util.List;

public class NumberListActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private NumberDao numberDao;

    private TextView mTextViewNumberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_number_list);

        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "literacyapp-db", null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        numberDao = daoSession.getNumberDao();

        mTextViewNumberList = (TextView) findViewById(R.id.textViewNumberList);
    }

    @Override
    protected void onStart() {
        Log.d(getClass(), "onStart");
        super.onStart();

        String numberListText = "";
        List<Number> numbers = numberDao.loadAll();
        Log.d(getClass(), "numbers.size(): " + numbers.size());
        for (Number number : numbers) {
            numberListText += "id: " + number.getId() + ", language: " + number.getLocale().getLanguage() + ",  value: " + number.getValue() + ", word: " + number.getWord() + "\n";
        }
        mTextViewNumberList.setText(numberListText);
    }
}
