package org.hertsstack.example.codegents;

import org.hertsstack.core.modelx.HertsMessage;

import java.util.Date;

public class User extends HertsMessage {
    private String id;
    private String name;
    private Date createdAt;
}
