package org.herts.metrics;

/**
 * Herts metrics context
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsMetricsContext {
    public enum Metric {
        Rps,
        ErrRate
    }

    public static final String METRICS_KEY = "MethodName";
    public static final String HTTP_REQ_LATENCY = "http.requests.latency";
    public static final String HTTP_REQ_COUNT = "http.requests.count";
    public static final String HTTP_REQ_TIMER = "http.requests.timer";
    public static final String HTTP_REQ_ERR_RATE = "http.requests.error.rate";

    public static final String RPC_CMD_LATENCY = "rpc.cmd.latency";
    public static final String RPC_CMD_COUNT = "rpc.cmd.count";
    public static final String RPC_CMD_TIMER = "rpc.cmd.timer";
    public static final String RPC_CMD_ERR_RATE = "rpc.cmd.error.rate";
}
