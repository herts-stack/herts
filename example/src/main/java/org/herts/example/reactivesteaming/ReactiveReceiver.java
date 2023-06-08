package org.herts.example.reactivesteaming;

import org.herts.core.annotation.HertsRpcReceiver;
import org.herts.core.service.HertsReceiver;

@HertsRpcReceiver
public interface ReactiveReceiver extends HertsReceiver {
    void onReceived(String value);
}
