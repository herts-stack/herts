package com.tomoyane.herts.hertscommon.util;

import java.util.Date;

public class DateTimeUtil {
    private static final Date currentDate = new Date();

    public static long getCurrentTimeMilliSec() {
        return currentDate.getTime();
    }
}
