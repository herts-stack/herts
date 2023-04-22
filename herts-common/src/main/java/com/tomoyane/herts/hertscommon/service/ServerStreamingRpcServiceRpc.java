package com.tomoyane.herts.hertscommon.service;

import com.tomoyane.herts.hertscommon.context.HertsType;

/**
 * Herts server streaming service
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class ServerStreamingRpcServiceRpc extends HertsRpcRpcBase {

    public ServerStreamingRpcServiceRpc() {
        super(HertsType.ServerStreaming);
    }
}
