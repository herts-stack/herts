package com.tomoyane.herts.hertscoreclient;

/**
 * Herts core client gRPC option
 * Supported options.
 * - idleTimeoutMilliSec
 * - keepaliveMilliSec
 * - keepaliveTimeoutMilliSec
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class GrpcClientOption {
    private Long idleTimeoutMilliSec;
    public void setIdleTimeoutMilliSec(long idleTimeoutMilliSec) {
        this.idleTimeoutMilliSec = idleTimeoutMilliSec;
    }

    public Long getIdleTimeoutMilliSec() {
        return idleTimeoutMilliSec;
    }

    private Long keepaliveMilliSec;
    public Long getKeepaliveMilliSec() {
        return keepaliveMilliSec;
    }

    public void setKeepaliveMilliSec(long keepaliveMilliSec) {
        this.keepaliveMilliSec = keepaliveMilliSec;
    }

    private Long keepaliveTimeoutMilliSec;
    public Long getKeepaliveTimeoutMilliSec() {
        return keepaliveTimeoutMilliSec;
    }

    public void setKeepaliveTimeoutMilliSec(long keepaliveTimeoutMilliSec) {
        this.keepaliveTimeoutMilliSec = keepaliveTimeoutMilliSec;
    }
}
