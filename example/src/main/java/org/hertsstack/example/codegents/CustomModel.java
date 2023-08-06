package org.hertsstack.example.codegents;

import org.hertsstack.core.modelx.HertsMessage;

public class CustomModel extends HertsMessage {
    private int id;
    private String data;
    private NestedCustomModel nestedCustomModel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public NestedCustomModel getNestedCustomModel() {
        return nestedCustomModel;
    }

    public void setNestedCustomModel(NestedCustomModel nestedCustomModel) {
        this.nestedCustomModel = nestedCustomModel;
    }

    public static class NestedCustomModel extends HertsMessage {
        private long nestedId;
        private String pointer;

        public long getNestedId() {
            return nestedId;
        }

        public void setNestedId(long nestedId) {
            this.nestedId = nestedId;
        }

        public String getPointer() {
            return pointer;
        }

        public void setPointer(String pointer) {
            this.pointer = pointer;
        }
    }
}
