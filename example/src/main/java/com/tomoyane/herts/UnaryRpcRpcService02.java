package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsRpcService;

public interface UnaryRpcRpcService02 extends HertsRpcService {
    String hello01(String id, String value);
}
