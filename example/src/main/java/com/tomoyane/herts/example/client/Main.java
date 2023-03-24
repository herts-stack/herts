package com.tomoyane.herts.example.client;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        try {
//            ClientImplUtil.unary();
//            ClientImplUtil.bidirectionalStreaming();
//            ClientImplUtil.serverStreaming();
            ClientImplUtil.clientStreaming();
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}