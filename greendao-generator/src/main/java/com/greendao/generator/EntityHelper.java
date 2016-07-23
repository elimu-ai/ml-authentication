package com.greendao.generator;

import org.literacyapp.model.enums.Locale;
import org.literacyapp.model.enums.content.ImageType;
import org.literacyapp.model.gson.content.WordGson;

import java.lang.reflect.Field;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class EntityHelper {

    public static Entity createEntityFromClass(Class clazz, Schema schema) {
        Entity entity = null;

        String className = clazz.getSimpleName();
        if (className.endsWith("Gson")) {
            className = className.replace("Gson", "");
        }
        System.out.println("className: " + className);
        entity = schema.addEntity(className);

        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("class name: " + className + ", field type: " + field.getType() + ", field name: " + field.getName());

            // TODO: detect if Enum is from inside "org.literacyapp.model" package
            if (field.getType().isAssignableFrom(String.class)
                    || field.getType().isAssignableFrom(Locale.class)
                    || field.getType().isAssignableFrom(ImageType.class)) {
                if (field.getType().isAssignableFrom(Locale.class)) {
                    entity.addStringProperty(field.getName()).customType(Locale.class.getCanonicalName(), "org.literacyapp.dao.converter.LocaleConverter");
                } else {
                    entity.addStringProperty(field.getName());
                }
            } else if (field.getType().isAssignableFrom(Long.class)) {
                if ("id".equals(field.getName())) {
                    entity.addIdProperty().primaryKey();
                } else {
                    entity.addLongProperty(field.getName());
                }
            } else if (field.getType().isAssignableFrom(Integer.class)) {
                entity.addIntProperty(field.getName());
            } else if (field.getType() == WordGson.class) {
                // See http://greenrobot.org/greendao/documentation/relations/#Modelling_To-One_Relations
                Property wordIdProperty = entity.addLongProperty("wordId").getProperty();
                int index = 0;
                for (Entity schemaEntity : schema.getEntities()) {
                    if (schemaEntity.getClassName().equals("Word")) {
                        Entity entityWord = schema.getEntities().get(index);
                        entity.addToOne(entityWord, wordIdProperty);
                    }
                    index++;
                }
            } else {
                System.err.println("Missing type support must be added: " + field.getType());
                entity = null;
            }
        }

        return entity;
    }
}
