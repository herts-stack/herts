package org.herts.example.duplexstreaming_rpc;

import org.herts.common.annotation.HertsRpcReceiver;
import org.herts.common.service.HertsReceiver;

@HertsRpcReceiver
public interface DuplexStreamingReceiver extends HertsReceiver {
    void onReceivedHello02();
}
