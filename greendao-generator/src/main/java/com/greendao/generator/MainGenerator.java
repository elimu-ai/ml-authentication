package com.greendao.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {

    /**
     * This file is executed when a Gradle build is triggered from the project's root directory.
     * E.g. "./gradlew clean build"
     */
    public static void main(String [] args) {
        System.out.println("greendao-generator main");

        Schema schema = new Schema(1, "org.literacyapp.dao");

        Entity number = schema.addEntity("Number");
        number.addIdProperty().autoincrement().primaryKey();
        number.addStringProperty("language");
        number.addIntProperty("value");
        // TODO: map Entity automatically from org.literacyapp.model.json.Number

        // TODO: Letter

        // TODO: Word

        // TODO: Audio

        // TODO: Image

        try {
            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(schema, "../app/src/main/java");

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
