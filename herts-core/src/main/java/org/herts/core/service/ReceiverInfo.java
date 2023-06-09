package org.herts.core.service;

/**
 * Herts receiver info
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ReceiverInfo {
    private final HertsReceiver receiver;
    private final HertsReactiveStreamingInvoker invoker;

    public ReceiverInfo(HertsReceiver receiver, HertsReactiveStreamingInvoker invoker) {
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
