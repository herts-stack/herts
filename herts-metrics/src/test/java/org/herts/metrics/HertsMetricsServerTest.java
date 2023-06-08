package org.herts.metrics;

import org.herts.core.service.HertsService;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.HertsMetricsServer;
import org.herts.metrics.TestHertsServiceImpl;
import org.herts.metrics.HertsMetricsHandler;
import org.junit.jupiter.api.BeforeAll;

import java.util.Collections;
import java.util.List;

public class HertsMetricsServerTest {
    private static HertsMetricsServer server;

    @BeforeAll
    static void init() {
        List<HertsService> services = Collections.singletonList(new TestHertsServiceImpl());
        HertsMetrics hertsMetrics = HertsMetricsHandler.builder()
                .registerHertsServices(services)
                .isJvmEnabled(true)
                .isLatencyEnabled(true)
                .isErrRateEnabled(true)
                .isRpsEnabled(true)
                .isServerResourceEnabled(true)
                .build();

        server = new HertsMetricsServer(hertsMetrics);
    }
}
