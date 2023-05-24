package org.herts.common.context;

/**
 * HertsClientInfo
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsClientInfo extends HertsMsg {
    public String id;
    public String globalIp;
    public String userAgent;
    public String javaVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlobalIp() {
        return globalIp;
    }

    public void setGlobalIp(String globalIp) {
        this.globalIp = globalIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }
}
