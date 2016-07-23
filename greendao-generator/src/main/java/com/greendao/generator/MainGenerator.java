package com.greendao.generator;

import org.literacyapp.model.gson.content.AudioGson;
import org.literacyapp.model.gson.content.ImageGson;
import org.literacyapp.model.gson.content.LetterGson;
import org.literacyapp.model.gson.content.NumberGson;
import org.literacyapp.model.gson.content.WordGson;

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

        int versionCode = 1001006; // 1.1.6 (this should match the version of the dependency org.literacyapp:literacyapp-model)
        // NOTE: If you increase the schema version, all tables in the database will be deleted!
        // TODO: handle database migration when upgrade to new schema version
        Schema schema = new Schema(versionCode, "org.literacyapp.dao");

        // TODO: Allophone
        Entity letter = EntityHelper.createEntityFromClass(LetterGson.class, schema);
        Entity word = EntityHelper.createEntityFromClass(WordGson.class, schema);
        Entity number = EntityHelper.createEntityFromClass(NumberGson.class, schema);
        Entity audio = EntityHelper.createEntityFromClass(AudioGson.class, schema);
        Entity image = EntityHelper.createEntityFromClass(ImageGson.class, schema);

        try {
            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(schema, "../app/src/main/java");
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
