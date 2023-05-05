package org.herts.common.context;

public class HertsMetricsSetting {
    private boolean isRpsEnabled;
    private boolean isLatencyEnabled;
    private boolean isErrRateEnabled;
    private boolean isServerResourceEnabled;
    private boolean isJvmEnabled;

    public HertsMetricsSetting(boolean isRpsEnabled, boolean isLatencyEnabled,
                               boolean isErrRateEnabled, boolean isServerResourceEnabled, boolean isJvmEnabled) {
        this.isRpsEnabled = isRpsEnabled;
        this.isLatencyEnabled = isLatencyEnabled;
        this.isErrRateEnabled = isErrRateEnabled;
        this.isServerResourceEnabled = isServerResourceEnabled;
        this.isJvmEnabled = isJvmEnabled;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean isRpsEnabled = false;
        private boolean isLatencyEnabled = false;
        private boolean isErrRateEnabled = false;
        private boolean isServerResourceEnabled = false;
        private boolean isJvmEnabled = false;

        public Builder() {}

        public Builder isRpsEnabled(boolean isRpsEnabled) {
            this.isRpsEnabled = isRpsEnabled;
            return this;
        }

        public Builder isLatencyEnabled(boolean isLatencyEnabled) {
            this.isLatencyEnabled = isLatencyEnabled;
            return this;
        }

        public Builder isErrRateEnabled(boolean isErrRateEnabled) {
            this.isErrRateEnabled = isErrRateEnabled;
            return this;
        }

        public Builder isServerResourceEnabled(boolean isServerResourceEnabled) {
            this.isServerResourceEnabled = isServerResourceEnabled;
            return this;
        }

        public Builder isJvmEnabled(boolean isJvmEnabled) {
            this.isJvmEnabled = isJvmEnabled;
            return this;
        }

        public HertsMetricsSetting build() {
            return new HertsMetricsSetting(this.isRpsEnabled, this.isLatencyEnabled, this.isErrRateEnabled, this.isServerResourceEnabled, this.isJvmEnabled);
        }
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
