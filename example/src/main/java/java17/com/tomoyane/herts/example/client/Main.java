package java17.com.tomoyane.herts.example.client;

import com.tomoyane.herts.ClientServiceExample;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        try {
//            ClientServiceExample.unary();
            ClientServiceExample.bidirectionalStreaming();
//            ClientServiceExample.serverStreaming();
//            ClientServiceExample.clientStreaming();
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}