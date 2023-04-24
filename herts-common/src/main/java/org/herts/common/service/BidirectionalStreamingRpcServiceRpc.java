package org.herts.common.service;

import org.herts.common.context.HertsType;

/**
 * Herts bidirectional streaming service
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class BidirectionalStreamingRpcServiceRpc extends HertsRpcRpcBase {

    public BidirectionalStreamingRpcServiceRpc() {
        super(HertsType.BidirectionalStreaming);
    }
}
