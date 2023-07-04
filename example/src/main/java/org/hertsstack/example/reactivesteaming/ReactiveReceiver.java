package org.hertsstack.example.reactivesteaming;

import org.hertsstack.core.annotation.HertsRpcReceiver;
import org.hertsstack.core.service.HertsReceiver;

@HertsRpcReceiver
public interface ReactiveReceiver extends HertsReceiver {
    void onReceived(String value);
}
