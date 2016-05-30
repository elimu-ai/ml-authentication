package com.greendao.generator;

import org.literacyapp.model.json.Audio;
import org.literacyapp.model.json.Image;
import org.literacyapp.model.json.Letter;
import org.literacyapp.model.json.Word;

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

        int versionCode = 1000013; // 1.0.13 (this should match the version of the dependency org.literacyapp:literacyapp-model)
        Schema schema = new Schema(versionCode, "org.literacyapp.dao");

        // TODO: handle database migration when upgrade to new schema version

        Entity word = EntityHelper.createEntityFromClass(Word.class, schema);
        Entity number = EntityHelper.createEntityFromClass(org.literacyapp.model.json.Number.class, schema);
        Entity letter = EntityHelper.createEntityFromClass(Letter.class, schema);
        Entity audio = EntityHelper.createEntityFromClass(Audio.class, schema);
        Entity image = EntityHelper.createEntityFromClass(Image.class, schema);

        try {
            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(schema, "../app/src/main/java");
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
