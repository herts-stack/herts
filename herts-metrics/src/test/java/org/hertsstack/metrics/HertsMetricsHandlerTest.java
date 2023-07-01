package org.hertsstack.metrics;

import org.hertsstack.core.service.HertsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HertsMetricsHandlerTest {
    private static HertsMetrics hertsMetrics;

    @BeforeAll
    static void init() {
        List<HertsService> services = Collections.singletonList(new TestHertsServiceImpl());
        hertsMetrics = HertsMetricsHandler.builder()
                .registerHertsServices(services)
                .isJvmEnabled(true)
                .isLatencyEnabled(true)
                .isErrRateEnabled(true)
                .isRpsEnabled(true)
                .isServerResourceEnabled(true)
                .build();
    }

    @Test
    public void getRegisteredMetricsType() {
        assertEquals(MetricsType.Prometheus, hertsMetrics.getRegisteredMetricsType());
    }

    @Test
    public void register() {
        hertsMetrics.register();
    }

    @Test
    public void getLatencyTimer() {
        assertNotNull(hertsMetrics.getLatencyTimer());
    }

    @Test
    public void isRpsEnabled() {
        assertTrue(hertsMetrics.isRpsEnabled());
    }

    @Test
    public void isLatencyEnabled() {
        assertTrue(hertsMetrics.isLatencyEnabled());
    }

    @Test
    public void isErrRateEnabled() {
        assertTrue(hertsMetrics.isErrRateEnabled());
    }

    @Test
    public void isServerResourceEnabled() {
        assertTrue(hertsMetrics.isServerResourceEnabled());
    }

    @Test
    public void isJvmEnabled() {
        assertTrue(hertsMetrics.isJvmEnabled());
    }

    @Test
    public void isMetricsEnabled() {
        assertTrue(hertsMetrics.isMetricsEnabled());
    }

    @Test
    public void getPrometheusFormat() {
        assertNotNull(hertsMetrics.getPrometheusFormat());
    }
}
