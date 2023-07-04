package org.hertsstack.e2etest.common;

import java.io.Serializable;

public class HelloResponse02 implements Serializable {
    private String a01;
    private int b01;

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
}
