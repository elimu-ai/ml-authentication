package com.greendao.generator;

import org.literacyapp.model.enums.ImageType;
import org.literacyapp.model.enums.Language;

import java.lang.reflect.Field;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.PropertyType;
import de.greenrobot.daogenerator.Schema;

public class EntityHelper {

    public static Entity createEntityFromClass(Class clazz, Schema schema) {
        Entity entity = null;

        String className = clazz.getSimpleName();
        System.out.println("className: " + className);
        entity = schema.addEntity(className);

        entity.addIdProperty().autoincrement().primaryKey();

        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("class name: " + className + ", field type: " + field.getType() + ", field name: " + field.getName());

            if ("id".equals(field.getName())) {
                entity.addLongProperty("serverId");
                continue;
            }

            if (field.getType().isAssignableFrom(String.class)
                    || field.getType().isAssignableFrom(Language.class)
                    || field.getType().isAssignableFrom(ImageType.class)) {
                entity.addStringProperty(field.getName());
            } else if (field.getType().isAssignableFrom(Long.class)) {
                entity.addLongProperty(field.getName());
            } else if (field.getType().isAssignableFrom(Integer.class)) {
                entity.addIntProperty(field.getName());
            } else {
                System.err.println("Missing type support must be added: " + field.getType());
                entity = null;
            }
        }

        return entity;
    }
}
