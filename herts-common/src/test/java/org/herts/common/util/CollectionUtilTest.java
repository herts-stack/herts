package org.herts.common.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionUtilTest {

    @Test
    public void findDuplicates() {
        var list = Arrays.asList("a", "b", "b");
        var dupList = CollectionUtil.findDuplicates(list);

        for (String data : dupList) {
            assertEquals("b", data);
        }
    }
}
