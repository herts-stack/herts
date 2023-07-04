package org.hertsstack.metrics;

import org.hertsstack.core.service.HertsService;
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
