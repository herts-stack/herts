package org.herts.core.modelx;

/**
 * Internal Payload.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class InternalHttpMsg {
    private String keyName;
    private Object value;
    private String classInfo;

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    public String getKeyName() {
        return keyName;
    }

    public Object getValue() {
        return value;
    }

    public String getClassInfo() {
        return classInfo;
    }
}


