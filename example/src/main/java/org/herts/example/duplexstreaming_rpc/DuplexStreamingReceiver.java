package org.herts.example.duplexstreaming_rpc;

import org.herts.common.annotation.HertsRpcReceiver;
import org.herts.common.service.HertsReceiver;

import java.util.Map;

@HertsRpcReceiver
public interface DuplexStreamingReceiver extends HertsReceiver {
    void onReceivedHello01();

    void onReceivedHello02(String a, int b);

    void onReceivedHello03(Map<String, String> a);
}
