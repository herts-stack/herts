package org.hertsstack.http;

import org.hertsstack.core.modelx.RegisteredMethod;
import org.hertsstack.serializer.MessageSerializer;
import org.hertsstack.metrics.HertsMetrics;
import org.hertsstack.metrics.HertsMetricsContext;
import org.hertsstack.metrics.HertsTimer;
import org.hertsstack.metrics.HertsMetricsServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http with measurer metrics class
 *
 * @author Herts Contributer
 */
class HertsHttpMetricsCaller extends HertsHttpCallerBase implements InternalHttpCaller {

    private final Object coreObject;
    private final String serviceName;
    private final HertsMetrics hertsMetrics;
    private final MessageSerializer hertsSerializer;

    public HertsHttpMetricsCaller(Object coreObject, HertsMetrics hertsMetrics,
                                  MessageSerializer hertsSerializer, HertsMetricsServer hertsMetricsServer,
                                  ConcurrentMap<String, RegisteredMethod> registeredMethods, String serviceName) {

        super(coreObject, hertsMetricsServer, hertsSerializer, registeredMethods);
        this.coreObject = coreObject;
        this.serviceName = serviceName;
        this.hertsMetrics = hertsMetrics;
        this.hertsSerializer = hertsSerializer;
    }

    @Override
    public void post(String serviceName, Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
