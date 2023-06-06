package org.herts.http.http;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;

public class HertsHttpRequestImpl implements HertsHttpRequest {
    private ServletRequest request;
    private HttpServletRequest httpRequest;
    private Enumeration<String> headerNames;

    @SuppressWarnings("unchecked")
    public HertsHttpRequestImpl(ServletRequest request, HttpServletRequest httpRequest) {
        this.request = request;
        this.httpRequest = httpRequest;
        this.headerNames = this.httpRequest.getHeaderNames();
    }

    public ServletRequest getRequest() {
        return request;
    }

    public String getHeader(String headerName) {
        String val = this.httpRequest.getHeader(headerName);
        if (val != null && !val.equals("")) {
            return val;
        }
        return null;
    }

    public String getRemoteAddr() {
        return this.request.getRemoteAddr();
    }

    public String getLocalAddr() {
        return this.request.getLocalAddr();
    }

    public String getAuthType() {
        return this.httpRequest.getAuthType();
    }

    public Cookie[] getCookies() {
        Cookie[] cookie = new Cookie[this.httpRequest.getCookies().length];
        int index = 0;
        for (javax.servlet.http.Cookie c : this.httpRequest.getCookies()) {
            cookie[index] = new Cookie(
                    c.getName(),
                    c.getValue(),
                    c.getComment(),
                    c.getDomain(),
                    c.getMaxAge(),
                    c.getPath(),
                    c.getSecure(),
                    c.getVersion()
            );
            index++;
        }
        return cookie;
    }

    public static class Cookie {
        private String name;                // NAME= ... "$Name" style is reserved
        private String value;               // value of NAME
        private String comment;             // ;Comment=VALUE ... describes cookie's use
        private String domain;              // ;Domain=VALUE ... domain that sees cookie
        private int maxAge = -1;            // ;Max-Age=VALUE ... cookies auto-expire
        private String path;                // ;Path=VALUE ... URLs that see the cookie
        private boolean secure;             // ;Secure ... e.g. use SSL
        private int version = 0;            // ;Version=1 ... means RFC 2109++ style

        public Cookie(String name, String value, String comment, String domain, int maxAge,
                      String path, boolean secure, int version) {
            this.name = name;
            this.value = value;
            this.comment = comment;
            this.domain = domain;
            this.maxAge = maxAge;
            this.path = path;
            this.secure = secure;
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getComment() {
            return comment;
        }

        public String getDomain() {
            return domain;
        }

        public int getMaxAge() {
            return maxAge;
        }

        public String getPath() {
            return path;
        }

        public boolean isSecure() {
            return secure;
        }

        public int getVersion() {
            return version;
        }
    }
}
