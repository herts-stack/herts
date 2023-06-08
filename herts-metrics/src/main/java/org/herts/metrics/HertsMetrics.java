package org.herts.metrics;

import io.micrometer.core.instrument.Timer;

import java.util.concurrent.ConcurrentMap;

/**
 * Herts core metrics interface
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsMetrics {

    /**
     * Registration metrics settings
     * Support prometheus
     */
    void register();

    /**
     * Get registration metrics type
     *
     * @return MetricsType
     */
    MetricsType getRegisteredMetricsType();

    /**
     * Get latency metrics timer map
     *
     * @return ConcurrentMap
     */
    ConcurrentMap<String, Timer> getLatencyTimer();

    /**
     * Counter metrics
     *
     * @param metric HertsMetricsContext.Metric
     * @param method Method name
     */
    void counter(HertsMetricsContext.Metric metric, String method);

    /**
     * Metrics rps is enabled or not
     *
     * @return enable or not
     */
    boolean isRpsEnabled();

    /**
     * Metrics latency is enabled or not
     *
     * @return enable or not
     */
    boolean isLatencyEnabled();

    /**
     * Metrics err rate is enabled or not
     *
     * @return enable or not
     */
    boolean isErrRateEnabled();

    /**
     * Metrics server resource is enabled or not
     *
     * @return enable or not
     */
    boolean isServerResourceEnabled();

    /**
     * Metrics jvm is enabled or not
     *
     * @return enable or not
     */
    boolean isJvmEnabled();

    /**
     * Metrics is enabled or not
     *
     * @return enable or not
     */
    boolean isMetricsEnabled();

    /**
     * Scrape wrapper function
     *
     * @return scrape information
     */
    String scrape();

    /**
     * Get prometheus format string
     *
     * @return Text format
     */
    String getPrometheusFormat();

    /**
     * Start latency timer by method
     *
     * @param method Method name
     * @return HertsTimer
     */
    HertsTimer startLatencyTimer(String method);

    /**
     * Stop latency timer
     *
     * @param timer HertsTimer
     */
    void stopLatencyTimer(HertsTimer timer);
}