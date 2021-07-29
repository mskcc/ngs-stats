package org.mskcc.permissions;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryPermissionsControllerTest {

    private static Logger log = LoggerFactory.getLogger(DeliveryPermissionsControllerTest.class);

    @Test
    void buildGroupAccessList() {
        log.info("RUNNING buildGroupAccessListTest");

        List<String> groups = DeliveryPermissionsController.buildGroupAccessList(true, false,
                "ME@HERE.COM");
        assertEquals(DeliveryPermissionsController.CMO_GROUP, groups.get(0));

        groups = DeliveryPermissionsController.buildGroupAccessList(false, true,
                "ME@HERE.COM");
        assertEquals(DeliveryPermissionsController.BIC_GROUP, groups.get(0));

        groups = DeliveryPermissionsController.buildGroupAccessList(false, false,
                "ME@HERE.COM,zzPDL_SKI_ISABL@mskcc.org");
        assertEquals(DeliveryPermissionsController.ISABL_GROUP, groups.get(0));
    }

    @Test
    void getFourDigitRequest() {
        System.out.println("Testing removing leading zero from project numbers.");
        // check leading zero is removed
        assertEquals("1234", DeliveryPermissionsController.getFourDigitRequest("01234"));
        assertEquals("1234_AH", DeliveryPermissionsController.getFourDigitRequest("01234_AH"));
        assertEquals("12345", DeliveryPermissionsController.getFourDigitRequest("12345"));
    }

    @Test
    void getDataAccessIDsRemoveNonmskcc() {
        String test = "jah@med.cornell.edu,david@mskcc.org";
        List<String> result = DeliveryPermissionsController.getDataAccessIDs(test);
        assertEquals("david", result.get(0));
    }
    @Test
    void getDataAccessIDsRemovezzPDL() {
        String test = "skicmopm@mskcc.org,zzPDL_SKI_CMO_ACCESS@mskcc.org,";
        List<String> result = DeliveryPermissionsController.getDataAccessIDs(test);
        assertEquals("skicmopm", result.get(0));
        assertEquals(1, result.size());
    }


}