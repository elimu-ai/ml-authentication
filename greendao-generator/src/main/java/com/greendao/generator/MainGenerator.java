package com.greendao.generator;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {
    public static void main (String [] args) {
        Schema schema = new Schema(1, "com.literacyapp.android.db");

        Entity number = schema.addEntity("Number");

        //It's not neccesary to create a variable called Id
        //Also you can declare autoincrement id and primary key
        number.addIdProperty().autoincrement().primaryKey();
        number.addStringProperty("language");
        number.addIntProperty("value");

        try {
            DaoGenerator dg = new DaoGenerator();
            dg.generateAll(schema, "./app/src/main/java");

        }  catch (Exception e) {
            e.printStackTrace();
        }


    }
}
