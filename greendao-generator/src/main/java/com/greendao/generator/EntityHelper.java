package com.greendao.generator;

import org.literacyapp.model.enums.Locale;
import org.literacyapp.model.enums.content.AudioFormat;
import org.literacyapp.model.enums.content.ContentStatus;
import org.literacyapp.model.enums.content.ImageFormat;
import org.literacyapp.model.enums.content.VideoFormat;
import org.literacyapp.model.enums.content.allophone.ConsonantPlace;
import org.literacyapp.model.enums.content.allophone.ConsonantType;
import org.literacyapp.model.enums.content.allophone.ConsonantVoicing;
import org.literacyapp.model.enums.content.allophone.LipRounding;
import org.literacyapp.model.enums.content.allophone.SoundType;
import org.literacyapp.model.enums.content.allophone.VowelFrontness;
import org.literacyapp.model.enums.content.allophone.VowelHeight;
import org.literacyapp.model.enums.content.allophone.VowelLength;
import org.literacyapp.model.gson.StudentGson;
import org.literacyapp.model.gson.content.WordGson;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Set;

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

        if (clazz.getSuperclass() != null) {
            System.out.println("Superclass: " + clazz.getSuperclass().getSimpleName());
            if (clazz.getSuperclass().getSuperclass() != null) {
                System.out.println("Supersuperclass: " + clazz.getSuperclass().getSuperclass().getSimpleName());
                for (Field field : clazz.getSuperclass().getSuperclass().getDeclaredFields()) {
                    System.out.println("class name: " + clazz.getSuperclass().getSuperclass().getSimpleName() + ", field type: " + field.getType() + ", field name: " + field.getName());
                    addFieldToEntity(schema, entity, field);
                }
            }

            for (Field field : clazz.getSuperclass().getDeclaredFields()) {
                System.out.println("class name: " + clazz.getSuperclass().getSimpleName() + ", field type: " + field.getType() + ", field name: " + field.getName());
                addFieldToEntity(schema, entity, field);
            }

//            if ("MultimediaGson".equals(clazz.getSuperclass().getSimpleName())) {
//                entity.addByteArrayProperty("bytes");
//            }
        }

        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("class name: " + className + ", field type: " + field.getType() + ", field name: " + field.getName());
            addFieldToEntity(schema, entity, field);
        }

        return entity;
    }

    private static void addFieldToEntity(Schema schema, Entity entity, Field field) {
        if (field.getType().isAssignableFrom(Locale.class)) {
            entity.addStringProperty(field.getName()).customType(Locale.class.getCanonicalName(), "org.literacyapp.dao.converter.LocaleConverter");
        } else if (field.getType().isAssignableFrom(AudioFormat.class)) {
            entity.addStringProperty(field.getName()).customType(AudioFormat.class.getCanonicalName(), "org.literacyapp.dao.converter.AudioFormatConverter");
        } else if (field.getType().isAssignableFrom(ImageFormat.class)) {
            entity.addStringProperty(field.getName()).customType(ImageFormat.class.getCanonicalName(), "org.literacyapp.dao.converter.ImageFormatConverter");
        } else if (field.getType().isAssignableFrom(VideoFormat.class)) {
            entity.addStringProperty(field.getName()).customType(VideoFormat.class.getCanonicalName(), "org.literacyapp.dao.converter.VideoFormatConverter");
        } else if (field.getType().isAssignableFrom(SoundType.class)) {
            entity.addStringProperty(field.getName()).customType(SoundType.class.getCanonicalName(), "org.literacyapp.dao.converter.SoundTypeConverter");
        } else if (field.getType().isAssignableFrom(VowelLength.class)) {
            entity.addStringProperty(field.getName()).customType(VowelLength.class.getCanonicalName(), "org.literacyapp.dao.converter.VowelLengthConverter");
        } else if (field.getType().isAssignableFrom(VowelHeight.class)) {
            entity.addStringProperty(field.getName()).customType(VowelHeight.class.getCanonicalName(), "org.literacyapp.dao.converter.VowelHeightConverter");
        } else if (field.getType().isAssignableFrom(VowelFrontness.class)) {
            entity.addStringProperty(field.getName()).customType(VowelFrontness.class.getCanonicalName(), "org.literacyapp.dao.converter.VowelFrontnessConverter");
        } else if (field.getType().isAssignableFrom(LipRounding.class)) {
            entity.addStringProperty(field.getName()).customType(LipRounding.class.getCanonicalName(), "org.literacyapp.dao.converter.LipRoundingConverter");
        } else if (field.getType().isAssignableFrom(ConsonantType.class)) {
            entity.addStringProperty(field.getName()).customType(ConsonantType.class.getCanonicalName(), "org.literacyapp.dao.converter.ConsonantTypeConverter");
        } else if (field.getType().isAssignableFrom(ConsonantPlace.class)) {
            entity.addStringProperty(field.getName()).customType(ConsonantPlace.class.getCanonicalName(), "org.literacyapp.dao.converter.ConsonantPlaceConverter");
        } else if (field.getType().isAssignableFrom(ConsonantVoicing.class)) {
            entity.addStringProperty(field.getName()).customType(ConsonantVoicing.class.getCanonicalName(), "org.literacyapp.dao.converter.ConsonantVoicingConverter");
        } else if (field.getType().isAssignableFrom(ContentStatus.class)) {
            entity.addStringProperty(field.getName()).customType(ContentStatus.class.getCanonicalName(), "org.literacyapp.dao.converter.ContentStatusConverter");
        } else if (field.getType().isAssignableFrom(String.class)) {
            entity.addStringProperty(field.getName());
        } else if (field.getType().isAssignableFrom(Calendar.class)) {
            entity.addLongProperty(field.getName()).customType(Calendar.class.getCanonicalName(), "org.literacyapp.dao.converter.CalendarConverter");
        } else if (field.getType().isAssignableFrom(Long.class)) {
            if ("id".equals(field.getName())) {
                entity.addIdProperty().primaryKey();
            } else {
                entity.addLongProperty(field.getName());
            }
        } else if (field.getType().isAssignableFrom(Integer.class)) {
            entity.addIntProperty(field.getName());
        } else if (field.getType() == StudentGson.class) {
            // Add ID pointing to target entity
            // See http://greenrobot.org/greendao/documentation/relations/#Modelling_To-One_Relations
            Property studentIdProperty = entity.addLongProperty("studentId").getProperty();
            int index = 0;
            for (Entity schemaEntity : schema.getEntities()) {
                if (schemaEntity.getClassName().equals("Student")) {
                    Entity studentEntity = schema.getEntities().get(index);
                    entity.addToOne(studentEntity, studentIdProperty);
                }
                index++;
            }
        } else if (field.getType() == WordGson.class) {
            // Add ID pointing to target entity
            // See http://greenrobot.org/greendao/documentation/relations/#Modelling_To-One_Relations
            Property wordIdProperty = entity.addLongProperty("wordId").getProperty();
            int index = 0;
            for (Entity schemaEntity : schema.getEntities()) {
                if (schemaEntity.getClassName().equals("Word")) {
                    Entity wordEntity = schema.getEntities().get(index);
                    entity.addToOne(wordEntity, wordIdProperty);
                }
                index++;
            }
        } else if (field.getType().isAssignableFrom(Set.class)) {
            entity.addStringProperty(field.getName()).customType(Set.class.getCanonicalName(), "org.literacyapp.dao.converter.StringSetConverter");
        } else {
            entity = null;
            System.err.println("Missing type support: " + field.getType());
            throw new RuntimeException("Missing type support: " + field.getType());
        }
    }
}
