package com.greendao.generator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.literacyapp.model.json.Number;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class EntityHelperTest {

    @Test
    public void testCreateEntityFromClass() {
        Schema schema = new Schema(-1, "org.literacyapp.dao");
        Entity entity = EntityHelper.createEntityFromClass(Number.class, schema);
        assertNotNull(entity);

        System.out.println("entity.getProperties(): " + entity.getProperties());

        assertThat(entity.getProperties().size(), is(4));
        assertThat(entity.getProperties().get(0).getPropertyName(), is("id"));
        assertThat(entity.getProperties().get(1).getPropertyName(), is("serverId"));
        assertThat(entity.getProperties().get(2).getPropertyName(), is("language"));
        assertThat(entity.getProperties().get(3).getPropertyName(), is("value"));
    }
}
