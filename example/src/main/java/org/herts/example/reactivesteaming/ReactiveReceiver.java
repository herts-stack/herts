package org.herts.example.reactivesteaming;

import org.herts.common.annotation.HertsRpcReceiver;
import org.herts.common.reactive.HertsReceiver;

@HertsRpcReceiver
public interface ReactiveReceiver extends HertsReceiver {
    void onReceived(String value);
}
