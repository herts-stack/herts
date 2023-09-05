package org.hertsstack.example.codegents;

import org.hertsstack.core.modelx.HertsMessage;

import java.util.Date;

public class User extends HertsMessage {
    private String id;
    private String name;
    private Date createdAt;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
