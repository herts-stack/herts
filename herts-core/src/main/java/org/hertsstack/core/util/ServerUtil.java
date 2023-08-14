package org.hertsstack.core.util;

import org.hertsstack.core.context.HertsMetricsSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * Herts server utility
 *
 * @author Herts Contributer
 */
public class ServerUtil {
    private static final String[] HETRS_HTTP_METHODS = new String[]{"POST", "OPTIONS"};

    /**
     * Get http server endpoint log.
     *
     * @param endpoints endpoint list
     * @param metricsSetting HertsMetricsSetting
     * @return Log message list
     */
    public static List<String> getEndpointLogs(String[] endpoints, HertsMetricsSetting metricsSetting) {
        List<String> endpointLogs = new ArrayList<>();
        for (String endpoint : endpoints) {
            for (String m : HETRS_HTTP_METHODS) {
                String log = m.equals(HETRS_HTTP_METHODS[0]) ? "[" + m + "]    " : "[" + m + "] ";
                endpointLogs.add(log + endpoint);
            }
        }
        if (metricsSetting != null) {
            endpointLogs.add("[GET]     /metricsz" );
        }
        return endpointLogs;
    }
}
