package org.herts.common.cache;


import org.herts.common.service.HertsReactiveStreamingInvoker;
import org.herts.common.service.HertsReceiver;

public interface ReactiveStreamingReceiverCache {
    void setHertsReceiver(String hertsClientId, HertsReceiver hertsReceiver, HertsReactiveStreamingInvoker invoker);

    ReactiveStreamingReceiverCacheImpl.Receiver getHertsReceiver(String hertsClientId);
}