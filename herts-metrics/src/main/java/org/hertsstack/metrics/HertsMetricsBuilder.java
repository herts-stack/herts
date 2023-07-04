package org.hertsstack.metrics;

import org.hertsstack.core.service.HertsService;

import java.util.List;

/**
 * Herts core metrics builder
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsMetricsBuilder {

    /**
     * Set HertsCoreService
     *
     * @param hertsRpcServices HertsCoreService
     * @return Builder
     */
    HertsMetricsBuilder registerHertsServices(List<HertsService> hertsRpcServices);

    /**
     * Metrics rps is enabled or not
     *
     * @param isRpsEnabled Enable rps metrics
     * @return Builder
     */
    HertsMetricsBuilder isRpsEnabled(boolean isRpsEnabled);

    /**
     * Metrics latency is enabled or not
     *
     * @param isLatencyEnabled Enable latency metrics
     * @return Builder
     */
    HertsMetricsBuilder isLatencyEnabled(boolean isLatencyEnabled);

    /**
     * Metrics err rate is enabled or not
     *
     * @param isErrRateEnabled Enable error rate metrics
     * @return Builder
     */
    HertsMetricsBuilder isErrRateEnabled(boolean isErrRateEnabled);

    /**
     * Metrics jvm is enabled or not
     *
     * @param isJvmEnabled Enable JVM metrics
     * @return Builder
     */
    HertsMetricsBuilder isJvmEnabled(boolean isJvmEnabled);

    /**
     * Metrics server resource is enabled or not
     *
     * @param isServerResourceEnabled Enable Server machine resource
     * @return Builder
     */
    HertsMetricsBuilder isServerResourceEnabled(boolean isServerResourceEnabled);

    /**
     * Build
     *
     * @return HertsCoreMetrics
     */
    HertsMetrics build();
}
