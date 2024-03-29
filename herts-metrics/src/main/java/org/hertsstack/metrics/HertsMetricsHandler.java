package org.hertsstack.metrics;

import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.exception.TypeInvalidException;
import org.hertsstack.core.service.HertsService;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import io.prometheus.client.exporter.common.TextFormat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http metrics
 *
 * @author Herts Contributer
 */
public class HertsMetricsHandler implements HertsMetrics {
    private final ConcurrentMap<String, Tag> tagNames = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Timer> latencyTimer = new ConcurrentHashMap<>();
    private final MetricsType metricsType;
    private final PrometheusMeterRegistry prometheusMeterRegistry;
    private final List<Class<?>> hertsRpcServices;
    private final HertsType hertsType;
    private final boolean isRpsEnabled;
    private final boolean isLatencyEnabled;
    private final boolean isErrRateEnabled;
    private final boolean isServerResourceEnabled;
    private final boolean isJvmEnabled;

    private Boolean isMetricsEnabled = null;

    private HertsMetricsHandler(Builder builder) {
        this.hertsRpcServices = builder.hertsRpcServices;
        this.hertsType = builder.hertsType;
        this.metricsType = MetricsType.Prometheus;
        this.isRpsEnabled = builder.isRpsEnabled;
        this.isLatencyEnabled = builder.isLatencyEnabled;
        this.isErrRateEnabled = builder.isErrRateEnabled;
        this.isServerResourceEnabled = builder.isServerResourceEnabled;
        this.isJvmEnabled = builder.isJvmEnabled;

        switch (this.metricsType) {
            default:
                this.prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        }
    }

    public static HertsMetricsBuilder builder(HertsType hertsType) {
        return new Builder(hertsType);
    }

    public static class Builder implements HertsMetricsBuilder {
        private List<Class<?>> hertsRpcServices;
        private HertsType hertsType;
        private boolean isRpsEnabled = false;
        private boolean isLatencyEnabled = false;
        private boolean isErrRateEnabled = false;
        private boolean isServerResourceEnabled = false;
        private boolean isJvmEnabled = false;

        private Builder(HertsType hertsType) {
            this.hertsType = hertsType;
        }

        @Override
        public HertsMetricsBuilder registerHertsServices(List<HertsService> hertsRpcServices) {
            List<Class<?>> serviceClasses = new ArrayList<>();
            for (HertsService service : hertsRpcServices) {
                serviceClasses.add(service.getClass());
            }
            this.hertsRpcServices = serviceClasses;
            return this;
        }

        @Override
        public HertsMetricsBuilder registerHertsServicesByInterface(List<Class<?>> hertsRpcServices) {
            this.hertsRpcServices = hertsRpcServices;
            return this;
        }

        @Override
        public HertsMetricsBuilder isRpsEnabled(boolean isRpsEnabled) {
            this.isRpsEnabled = isRpsEnabled;
            return this;
        }

        @Override
        public HertsMetricsBuilder isLatencyEnabled(boolean isLatencyEnabled) {
            this.isLatencyEnabled = isLatencyEnabled;
            return this;
        }

        @Override
        public HertsMetricsBuilder isErrRateEnabled(boolean isErrRateEnabled) {
            this.isErrRateEnabled = isErrRateEnabled;
            return this;
        }

        @Override
        public HertsMetricsBuilder isJvmEnabled(boolean isJvmEnabled) {
            this.isJvmEnabled = isJvmEnabled;
            return this;
        }

        @Override
        public HertsMetricsBuilder isServerResourceEnabled(boolean isServerResourceEnabled) {
            this.isServerResourceEnabled = isServerResourceEnabled;
            return this;
        }

        @Override
        public HertsMetrics build() {
            if (this.hertsRpcServices == null || this.hertsRpcServices.size() == 0) {
                throw new TypeInvalidException("Required HertsCoreService");
            }
            return new HertsMetricsHandler(this);
        }
    }

    @Override
    public MetricsType getRegisteredMetricsType() {
        return this.metricsType;
    }

    @Override
    public void register() {
        for (Class<?> hertsRpcService : this.hertsRpcServices) {
            Method[] methods;
            try {
                String serviceName = hertsRpcService.getName();
                Class<?> thisClass = Class.forName(serviceName);
                methods = thisClass.getDeclaredMethods();
            } catch (Exception ex) {
                throw new TypeInvalidException("Herts service is invalid", ex);
            }

            for (Method method : methods) {
                ImmutableTag tag = new ImmutableTag(HertsMetricsContext.METRICS_KEY, method.getName());
                this.tagNames.put(method.getName(), tag);

                if (this.hertsType == HertsType.Http) {
                    this.latencyTimer.put(method.getName(), Timer.builder(HertsMetricsContext.HTTP_REQ_LATENCY)
                            .tags(HertsMetricsContext.METRICS_KEY, method.getName())
                            .register(this.prometheusMeterRegistry));
                } else {
                    this.latencyTimer.put(method.getName(), Timer.builder(HertsMetricsContext.RPC_CMD_LATENCY)
                            .tags(HertsMetricsContext.METRICS_KEY, method.getName())
                            .register(this.prometheusMeterRegistry));
                }
            }

            if (this.hertsType == HertsType.Http) {
                this.prometheusMeterRegistry.counter(HertsMetricsContext.HTTP_REQ_COUNT, this.tagNames.values());
                this.prometheusMeterRegistry.counter(HertsMetricsContext.HTTP_REQ_ERR_RATE, this.tagNames.values());
            } else {
                this.prometheusMeterRegistry.counter(HertsMetricsContext.RPC_CMD_COUNT, this.tagNames.values());
                this.prometheusMeterRegistry.counter(HertsMetricsContext.RPC_CMD_ERR_RATE, this.tagNames.values());
            }
        }

        Metrics.globalRegistry.add(this.prometheusMeterRegistry);
        if (this.isJvmEnabled) {
            new JvmMemoryMetrics().bindTo(this.prometheusMeterRegistry);
        }
        if (this.isServerResourceEnabled) {
            new ProcessorMetrics().bindTo(this.prometheusMeterRegistry);
        }
    }

    @Override
    public ConcurrentMap<String, Timer> getLatencyTimer() {
        return this.latencyTimer;
    }

    @Override
    public void counter(HertsMetricsContext.Metric metric, String method) {
        if (metric == HertsMetricsContext.Metric.Rps && this.hertsType == HertsType.Http) {
            this.prometheusMeterRegistry.counter(HertsMetricsContext.HTTP_REQ_COUNT, Collections.singleton(this.tagNames.get(method))).increment();
        } else if (metric == HertsMetricsContext.Metric.ErrRate && this.hertsType == HertsType.Http) {
            this.prometheusMeterRegistry.counter(HertsMetricsContext.HTTP_REQ_ERR_RATE, Collections.singleton(this.tagNames.get(method))).increment();
        } else if (metric == HertsMetricsContext.Metric.Rps) {
            this.prometheusMeterRegistry.counter(HertsMetricsContext.RPC_CMD_COUNT, Collections.singleton(this.tagNames.get(method))).increment();
        } else if (metric == HertsMetricsContext.Metric.ErrRate) {
            this.prometheusMeterRegistry.counter(HertsMetricsContext.RPC_CMD_ERR_RATE, Collections.singleton(this.tagNames.get(method))).increment();
        }
    }

    @Override
    public boolean isRpsEnabled() {
        return this.isRpsEnabled;
    }

    @Override
    public boolean isLatencyEnabled() {
        return this.isLatencyEnabled;
    }

    @Override
    public boolean isErrRateEnabled() {
        return this.isErrRateEnabled;
    }

    @Override
    public boolean isServerResourceEnabled() {
        return this.isServerResourceEnabled;
    }

    @Override
    public boolean isJvmEnabled() {
        return this.isJvmEnabled;
    }

    @Override
    public boolean isMetricsEnabled() {
        if (isMetricsEnabled != null) {
            return isMetricsEnabled;
        }
        isMetricsEnabled = isRpsEnabled() || isLatencyEnabled() || isErrRateEnabled() ||
                isServerResourceEnabled() || isJvmEnabled();
        return isMetricsEnabled;
    }

    @Override
    public String scrape() {
        return this.prometheusMeterRegistry.scrape();
    }

    @Override
    public String getPrometheusFormat() {
        return TextFormat.CONTENT_TYPE_OPENMETRICS_100;
    }

    @Override
    public HertsTimer startLatencyTimer(String method) {
        HertsTimer timer = new HertsTimer();
        timer.setType(HertsTimer.Type.Clock);
        timer.setMethod(method);
        timer.setSample(Timer.start(Clock.SYSTEM));
        return timer;
    }

    @Override
    public void stopLatencyTimer(HertsTimer timer) {
        timer.getSample().stop(this.latencyTimer.get(timer.getMethod()));
    }
}
