package org.hertsstack.rpcclient;

import org.hertsstack.core.modelx.HertsMessage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestFoo extends HertsMessage {
    private String a01;
    private int b01;
    private double c01;
    private Map<String, String> d01;
    private List<String> e01;
    private Set<String> f01;

    public String getA01() {
        return a01;
    }

    public void setA01(String a01) {
        this.a01 = a01;
    }

    public int getB01() {
        return b01;
    }

    public void setB01(int b01) {
        this.b01 = b01;
    }

    public double getC01() {
        return c01;
    }

    public void setC01(double c01) {
        this.c01 = c01;
    }

    public Map<String, String> getD01() {
        return d01;
    }

    public void setD01(Map<String, String> d01) {
        this.d01 = d01;
    }

    public List<String> getE01() {
        return e01;
    }

    public void setE01(List<String> e01) {
        this.e01 = e01;
    }

    public Set<String> getF01() {
        return f01;
    }

    public void setF01(Set<String> f01) {
        this.f01 = f01;
    }
}
