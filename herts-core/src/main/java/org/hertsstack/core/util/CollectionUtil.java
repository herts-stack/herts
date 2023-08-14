package org.hertsstack.core.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Herts collection utility
 *
 * @author Herts Contributer
 */
public class CollectionUtil {

    /**
     * Find duplicate entry.
     *
     * @param list List collection
     * @param <T>  Generics
     * @return Set collection
     */
    public static <T> Set<T> findDuplicates(List<T> list) {
        Set<T> seen = new HashSet<>();
        return list.stream()
                .filter(e -> !seen.add(e))
                .collect(Collectors.toSet());
    }
}
