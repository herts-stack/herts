package org.herts.common.cache;

import org.herts.common.service.HertsReactiveStreamingInvoker;
import org.herts.common.service.HertsReceiver;

import java.util.concurrent.ConcurrentHashMap;

public class ReactiveStreamingReceiverCacheImpl implements ReactiveStreamingReceiverCache {

    public static class Receiver {
        private final HertsReceiver receiver;
        private final HertsReactiveStreamingInvoker invoker;

        public Receiver(HertsReceiver receiver, HertsReactiveStreamingInvoker invoker) {
            this.receiver = receiver;
            this.invoker = invoker;
        }

        public HertsReceiver getReceiver() {
            return receiver;
        }

        public HertsReactiveStreamingInvoker getInvoker() {
            return invoker;
        }
    }

    private volatile ConcurrentHashMap<String, Receiver> receivers;
    private static ReactiveStreamingReceiverCache thisClass;

    public ReactiveStreamingReceiverCacheImpl() {
        this.receivers = new ConcurrentHashMap<>();
    }

    /**
     * Singleton instance.
     *
     * @return ReactiveStreamingReceiverCache
     */
    public static ReactiveStreamingReceiverCache getInstance() {
        if (thisClass != null) {
            return thisClass;
        }
        thisClass = new ReactiveStreamingReceiverCacheImpl();
        return thisClass;
    }

    @Override
    public void setHertsReceiver(String hertsClientId, HertsReceiver hertsReceiver, HertsReactiveStreamingInvoker invoker) {
        var receiver = new Receiver(hertsReceiver, invoker);
        this.receivers.put(hertsClientId, receiver);
    }

    @Override
    public Receiver getHertsReceiver(String hertsClientId) {
        return this.receivers.get(hertsClientId);
    }
}