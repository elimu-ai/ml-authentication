package org.literacyapp.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StudentHelperTest {

    @Test
    public void testExtractDeviceIdFromUniqueId() {
        String uniqueId = "4113947bec18b7ad_1";
        String deviceId = StudentHelper.extractDeviceIdFromUniqueId(uniqueId);
        assertThat(deviceId, is("4113947bec18b7ad"));
    }

    @Test
    public void testExtractLongIdFromUniqueId() {
        String uniqueId = "4113947bec18b7ad_1";
        Long longId = StudentHelper.extractLongIdFromUniqueId(uniqueId);
        assertThat(longId, is(1L));
    }
}
