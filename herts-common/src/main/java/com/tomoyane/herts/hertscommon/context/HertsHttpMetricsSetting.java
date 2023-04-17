package com.tomoyane.herts.hertscommon.context;

public class HertsHttpMetricsSetting {
    private boolean isRpsEnabled = false;
    private boolean isLatencyEnabled = false;
    private boolean isErrRateEnabled = false;
    private boolean isServerResourceEnabled = false;
    private boolean isJvmEnabled = false;

    public HertsHttpMetricsSetting(boolean isRpsEnabled, boolean isLatencyEnabled,
                                   boolean isErrRateEnabled, boolean isServerResourceEnabled, boolean isJvmEnabled) {
        this.isRpsEnabled = isRpsEnabled;
        this.isLatencyEnabled = isLatencyEnabled;
        this.isErrRateEnabled = isErrRateEnabled;
        this.isServerResourceEnabled = isServerResourceEnabled;
        this.isJvmEnabled = isJvmEnabled;
    }

    public boolean isRpsEnabled() {
        return isRpsEnabled;
    }

    public boolean isLatencyEnabled() {
        return isLatencyEnabled;
    }

    public boolean isErrRateEnabled() {
        return isErrRateEnabled;
    }

    public boolean isServerResourceEnabled() {
        return isServerResourceEnabled;
    }

    public boolean isJvmEnabled() {
        return isJvmEnabled;
    }
}
