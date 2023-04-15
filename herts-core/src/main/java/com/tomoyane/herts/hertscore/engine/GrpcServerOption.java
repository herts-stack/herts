package com.tomoyane.herts.hertscore.engine;

/**
 * GrpcServerOption for server
 * Supported options
 * - handshakeTimeoutMilliSec
 * - keepaliveTimeoutMilliSec
 * - keepaliveTimeMilliSec
 * - maxConnectionIdleMilliSec
 * - maxConnectionAgeMilliSec
 * - maxInboundMessageSize
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class GrpcServerOption {
    private int port = 9000;
    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    private Long handshakeTimeoutMilliSec;
    public void setHandshakeTimeoutMilliSec(long timeout) {
        this.handshakeTimeoutMilliSec = timeout;
    }

    public Long getHandshakeTimeoutMilliSec() {
        return handshakeTimeoutMilliSec;
    }

    private Long keepaliveTimeoutMilliSec;
    public void setKeepAliveTimeoutMilliSec(long timeout) {
        this.handshakeTimeoutMilliSec = timeout;
    }

    public Long getKeepaliveTimeoutMilliSec() {
        return keepaliveTimeoutMilliSec;
    }

    private Long keepaliveTimeMilliSec;
    public void setKeepAliveTimeMilliSec(long timeout) {
        this.keepaliveTimeMilliSec = timeout;
    }

    public Long getKeepaliveTimeMilliSec() {
        return keepaliveTimeMilliSec;
    }

    private Long maxConnectionIdleMilliSec;
    public void setMaxConnectionIdleMilliSec(long timeout) {
        this.maxConnectionIdleMilliSec = timeout;
    }

    public Long getMaxConnectionIdleMilliSec() {
        return maxConnectionIdleMilliSec;
    }

    private Long maxConnectionAgeMilliSec;
    public void setMaxConnectionAgeMilliSec(long timeout) {
        this.maxConnectionAgeMilliSec = timeout;
    }

    public Long getMaxConnectionAgeMilliSec() {
        return maxConnectionAgeMilliSec;
    }

    private int maxInboundMessageSize;
    public void setMaxInboundMessageSize(int bytes) {
        this.maxInboundMessageSize = bytes;
    }

    public int getMaxInboundMessageSize() {
        return maxInboundMessageSize;
    }
}
