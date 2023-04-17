package com.tomoyane.herts.hertsmetrics;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;

/**
 * Herts core metrics builder
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsMetricsBuilder {

    /**
     * Set HertsCoreService
     * @param hertsCoreService HertsCoreService
     * @return Builder
     */
    HertsMetricsBuilder hertsCoreServiceInterface(HertsCoreService hertsCoreService);

    /**
     * Metrics rps is enabled or not
     * @return Builder
     */
    HertsMetricsBuilder isRpsEnabled(boolean isRpsEnabled);

    /**
     * Metrics latency is enabled or not
     * @return Builder
     */
    HertsMetricsBuilder isLatencyEnabled(boolean isLatencyEnabled);

    /**
     * Metrics err rate is enabled or not
     * @return Builder
     */
    HertsMetricsBuilder isErrRateEnabled(boolean isErrRateEnabled);

    /**
     * Metrics jvm is enabled or not
     * @return Builder
     */
    HertsMetricsBuilder isJvmEnabled(boolean isJvmEnabled);

    /**
     * Metrics server resource is enabled or not
     * @return Builder
     */
    HertsMetricsBuilder isServerResourceEnabled(boolean isServerResourceEnabled);

    /**
     * Build
     * @return HertsCoreMetrics
     */
    HertsMetrics build();
}
