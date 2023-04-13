package com.tomoyane.herts.hertshttp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface HertsHttpServer {
    void doOptions(HttpServletRequest request, HttpServletResponse response);
    void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException;

    String[] getEndpoints();
}
