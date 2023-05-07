package org.herts.metrics.server;

import org.herts.common.service.HertsService;
import org.herts.metrics.TestHertsServiceImpl;
import org.herts.metrics.handler.HertsMetricsHandler;
import org.junit.jupiter.api.BeforeAll;

import java.util.Collections;
import java.util.List;

public class HertsMetricsServerTest {
    private static HertsMetricsServer server;

    @BeforeAll
    static void init() {
        List<HertsService> services = Collections.singletonList(new TestHertsServiceImpl());
        var hertsMetrics = HertsMetricsHandler.builder()
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
