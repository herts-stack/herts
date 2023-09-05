package org.hertsstack.example.http;

import org.hertsstack.core.modelx.HertsMessage;

public class TestModel extends HertsMessage {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
