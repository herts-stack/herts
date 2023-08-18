package org.hertsstack.e2etest.common;

import org.hertsstack.core.modelx.HertsMessage;

public class TestData extends HertsMessage {
    private String foo;
    private String bar;

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }
}
