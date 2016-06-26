package com.greendao.generator;

import org.literacyapp.model.json.AudioJson;
import org.literacyapp.model.json.ImageJson;
import org.literacyapp.model.json.LetterJson;
import org.literacyapp.model.json.NumberJson;
import org.literacyapp.model.json.WordJson;

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

        int versionCode = 1000015; // 1.0.15 (this should match the version of the dependency org.literacyapp:literacyapp-model)
        // NOTE: If you increase the schema version, all tables in the database will be deleted!
        // TODO: handle database migration when upgrade to new schema version
        Schema schema = new Schema(versionCode, "org.literacyapp.dao");

        Entity word = EntityHelper.createEntityFromClass(WordJson.class, schema);
        Entity number = EntityHelper.createEntityFromClass(NumberJson.class, schema);
        Entity letter = EntityHelper.createEntityFromClass(LetterJson.class, schema);
        Entity audio = EntityHelper.createEntityFromClass(AudioJson.class, schema);
        Entity image = EntityHelper.createEntityFromClass(ImageJson.class, schema);

        try {
            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(schema, "../app/src/main/java");
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
