package org.hertsstack.core.util;

import java.util.Date;

/**
 * Herts Date utility
 *
 * @author Herts Contributer
 */
public class DateTimeUtil {

    /**
     * Get current date milli times.
     *
     * @return long milli sec
     */
    public static long getCurrentTimeMilliSec() {
        Date currentDate = new Date();
        return currentDate.getTime();
    }
}
