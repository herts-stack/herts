package org.herts.httpclient;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TestDataModel {
    @JsonProperty
    private List<String> a;
    @JsonProperty
    private Map<String, String> b;
//    private HashSet<String> c;
    @JsonProperty
    private String d;
    @JsonProperty
    private int e;
    @JsonProperty
    private double f;
//    private float g;

    public List<String> getA() {
        return a;
    }

    public void setA(List<String> a) {
        this.a = a;
    }

    public Map<String, String> getB() {
        return b;
    }

    public void setB(Map<String, String> b) {
        this.b = b;
    }

//    public HashSet<String> getC() {
//        return c;
//    }
//
//    public void setC(HashSet<String> c) {
//        this.c = c;
//    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

//    public float getG() {
//        return g;
//    }
//
//    public void setG(float g) {
//        this.g = g;
//    }
}
