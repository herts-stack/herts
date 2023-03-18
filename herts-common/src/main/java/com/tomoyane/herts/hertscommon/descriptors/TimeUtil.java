package com.tomoyane.herts.hertscommon.descriptors;

import java.util.Date;

public class TimeUtil {
    private static final Date currentDate = new Date();

    public static long getCurrentTimeMilliSec() {
        return currentDate.getTime();
    }
}
