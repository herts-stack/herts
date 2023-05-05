package org.herts.metrics.context;

import io.micrometer.core.instrument.Timer;

/**
 * Herts timer entity
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsTimer {
    public enum Type {
        Clock
    }

    private Timer.Sample sample;
    private Type type;
    private String method;

    public Timer.Sample getSample() {
        return sample;
    }

    public void setSample(Timer.Sample sample) {
        this.sample = sample;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
