package org.herts.core.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionUtilTest {

    @Test
    public void findDuplicates() {
        List<String> list = Arrays.asList("a", "b", "b");
        Set<String> dupList = CollectionUtil.findDuplicates(list);

        for (String data : dupList) {
            assertEquals("b", data);
        }
    }
}
