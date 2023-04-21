package com.tomoyane.herts.hertscommon.service;

import com.tomoyane.herts.hertscommon.context.HertsType;

/**
 * Herts Http server service
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class HttpRpcServiceRpc extends HertsRpcRpcBase {

    public HttpRpcServiceRpc() {
        super(HertsType.Http);
    }
}
