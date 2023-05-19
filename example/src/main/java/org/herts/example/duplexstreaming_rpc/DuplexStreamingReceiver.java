package org.herts.example.duplexstreaming_rpc;

import org.herts.common.service.HertsReceiver;

public interface DuplexStreamingReceiver extends HertsReceiver {
    void onReceivedHello02();
}
