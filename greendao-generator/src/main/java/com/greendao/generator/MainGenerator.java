package com.greendao.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {

    public static void main (String [] args) {
        System.out.println("greendao-generator main");

        Schema schema = new Schema(1, "org.literacyapp.dao");

        Entity number = schema.addEntity("Number");
        number.addIdProperty().autoincrement().primaryKey();
        number.addStringProperty("language");
        number.addIntProperty("value");

        try {
            DaoGenerator dg = new DaoGenerator();
            dg.generateAll(schema, "../app/src/main/java");

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
