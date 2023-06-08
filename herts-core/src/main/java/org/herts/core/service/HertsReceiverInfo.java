package org.herts.core.service;

/**
 * Herts receiver info
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsReceiverInfo {
    private final HertsReceiver receiver;
    private final HertsReactiveStreamingInvoker invoker;

    public HertsReceiverInfo(HertsReceiver receiver, HertsReactiveStreamingInvoker invoker) {
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
