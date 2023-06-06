package org.herts.http;

import org.herts.common.serializer.HertsSerializer;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.context.HertsMetricsContext;
import org.herts.metrics.context.HertsTimer;

import org.herts.metrics.server.HertsMetricsServer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http with measurer metrics class
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpMetricsCaller extends HertsHttpCallerBase implements HertsHttpCaller {

    private final Object coreObject;
    private final String serviceName;
    private final HertsMetrics hertsMetrics;
    private final HertsSerializer hertsSerializer;

    public HertsHttpMetricsCaller(Object coreObject, HertsMetrics hertsMetrics,
                                  HertsSerializer hertsSerializer, HertsMetricsServer hertsMetricsServer,
                                  ConcurrentMap<String, List<Parameter>> parameters, String serviceName) {

        super(coreObject, hertsMetricsServer, hertsSerializer, parameters);
        this.coreObject = coreObject;
        this.serviceName = serviceName;
        this.hertsMetrics = hertsMetrics;
        this.hertsSerializer = hertsSerializer;
    }

    @Override
    public void post(Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HertsTimer timer = null;
        try {
            if (this.hertsMetrics.isLatencyEnabled()) {
                timer = this.hertsMetrics.startLatencyTimer(hertsMethod.getName());
            }
            if (this.hertsMetrics.isRpsEnabled()) {
                this.hertsMetrics.counter(HertsMetricsContext.Metric.Rps, hertsMethod.getName());
            }
            call(hertsMethod, request, response);
            if (this.hertsMetrics.isLatencyEnabled()) {
                this.hertsMetrics.stopLatencyTimer(timer);
            }
        } catch (Exception ex) {
            if (this.hertsMetrics.isErrRateEnabled()) {
                this.hertsMetrics.counter(HertsMetricsContext.Metric.ErrRate, hertsMethod.getName());
            }
            throw ex;
        }
    }
}
